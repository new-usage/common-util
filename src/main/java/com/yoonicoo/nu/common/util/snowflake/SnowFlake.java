package com.yoonicoo.nu.common.util.snowflake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author QingXu
 * @description Twitter开源分布式自增ID算法 snowflake
 *  SnowFlake的结构如下(每部分用-分开):<br>
 *  0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 *  1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 *  41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 *  得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 *  10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 *  12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 *  加起来刚好64位，为一个Long型。<br>
 *  SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * @date 2019/11/19 10:11
 */
public class SnowFlake {

    /** 起始时间戳，用于用当前时间戳减去这个时间戳，算出偏移量 **/
    private static final long START_STMP = 1519740777809L;

    /** 每部分占用的位数 **/
    private static final long WORKER_ID_BITS  = 5L; //机器标识占用的位数 (0-1023)
    private static final long DATACENTER_BITS = 5L; //数据中心占用的位数 (0-1023)
    private static final long SEQUENCE_BITS = 12L; //序列号占用的位数 (0-4095)

    /** 每一部分的最大值 */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS); //支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
    private static final long MAX_DATACENTER_ID  = -1L ^ (-1L << DATACENTER_BITS);//支持的最大数据标识id，结果是31
    private static final long SEQUENCE_MASK  = -1L ^ (-1L << SEQUENCE_BITS); //用mask防止溢出:位与运算保证计算的结果范围始终是 0-4095 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)

    private static final long WORKERID_OFFSET = SEQUENCE_BITS;
    private static final long DATACENTERID_OFFSET = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_OFFSET = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_BITS;


    private long workerId; //工作节点ID(0~31)
    private long datacenterId; //数据中心ID(0~31)
    private long sequence = 0L; //毫秒内序列(0~4095)
    private long lastTimestamp = -1L; //上次生成ID的时间截

    /**
     * 基于Snowflake创建分布式ID生成器
     *
     * @param workerId     工作机器ID,数据范围为0~31
     * @param datacenterId 数据中心ID,数据范围为0~31
     */
    public SnowFlake(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }
    /**
     * 获取ID
     *
     * @return
     */
    public synchronized Long nextId() {
        long timestamp = this.timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        // 解决跨毫秒生成ID序列号始终为偶数的缺陷:如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            // 通过位与运算保证计算的结果范围始终是 0-4095
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = this.tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        /*
         * 1.左移运算是为了将数值移动到对应的段(41、5、5，12那段因为本来就在最右，因此不用左移)
         * 2.然后对每个左移后的值(la、lb、lc、sequence)做位或运算，是为了把各个短的数据合并起来，合并成一个二进制数
         * 3.最后转换成10进制，就是最终生成的id
         */
        return ((timestamp - START_STMP) << TIMESTAMP_OFFSET) |
                (datacenterId << DATACENTERID_OFFSET) |
                (workerId << WORKERID_OFFSET) |
                sequence;
    }

    /**
     * 保证返回的毫秒数在参数之后(阻塞到下一个毫秒，直到获得新的时间戳)
     *
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }

        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     *
     * @return timestamp
     */
    private long timeGen() {
        return System.currentTimeMillis();
        /*if (isClock) {
            // 解决高并发下获取时间戳的性能问题
            return SystemClock.now();
        } else {
            return System.currentTimeMillis();
        }*/
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(1, 1);
        //线程池并行执行10000次ID生成
        ExecutorService executorService = Executors.newCachedThreadPool();;
        for (int i = 0; i < 10000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    long id = snowFlake.nextId();
                    System.out.println(id);
                }
            });
        }
        executorService.shutdown();
    }
}
