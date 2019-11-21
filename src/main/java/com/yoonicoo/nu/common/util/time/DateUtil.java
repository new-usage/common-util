package com.yoonicoo.nu.common.util.time;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QingXu
 * @Description：时间工具类  {@link DateUtils}
 * @date 2018年10月31日 下午4:54:02
 */
@Deprecated
public class DateUtil {

    public static String DATE_FORMAT = "yyyyMMdd";
    public static String DATE_FORMAT_ = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";
    public static String DATE_TIME_FORMAT_ = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_FORMAT = "hh:mm:ss";


    /**
     * SimpleDateFormat线程不安全
     * 解决方案：
     *  1、可以使用jdk8中的LocalDate、LocalDateTime （推荐使用）
     *  2、每次创建一个SimpleDateFormat：性能不好，浪费资源
     *  3、采用ThreadLocal每个线程拥有自己的SimpleDateFormat对象（推荐使用）
     *
     */


    //锁对象
    private static final Object lockObj = new Object();
    /**
     * 存放不同的日期模板格式的simpleDateFormate的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> simpleDateFormateMap= new HashMap<String, ThreadLocal<SimpleDateFormat>>();
    /**
     * 返回一个ThreadLocal的simpleDateFormate,每个线程只会new一次simpleDateFormate
     */
    private static SimpleDateFormat getSimpleDateFormate(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = simpleDateFormateMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = simpleDateFormateMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    simpleDateFormateMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSimpleDateFormate(pattern).format(date);
    }


    /**
     * @return String
     * @Description：获取当前8位日期
     * @author QingXu
     * @date 2018年11月1日 上午11:19:57
     */
    public static String getCurrDate() {
//        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return format(new Date(),DATE_FORMAT);
    }

    /**
     * @return String
     * @Description：获取当天17位时间
     * @author QingXu
     * @date 2018年11月1日 上午11:21:35
     */
    public static String getCurrDateTime() {
//        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return format(new Date(),DATE_TIME_FORMAT);
    }

    /**
     * @param pattern
     * @return String
     * @Description：根据格式获取当前时间
     * @author QingXu
     * @date 2018年11月1日 上午11:33:32
     */
    public static String getCurrDateTime(String pattern) {
//        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return format(new Date(),pattern);
    }

    public static void main(String[] args) {

    }


}
