package com.yoonicoo.nu.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Description：时间工具类 @ClassName： DateUtil
 * @ProjectName：common-util-use
 * @Package：com.yoonicoo.nu.common.util
 * @author QingXu
 * @date 2018年10月31日 下午4:54:02
 */
public class DateUtil {

	public static String DATE_FORMAT = "yyyyMMdd";
	public static String DATE_FORMAT_ = "yyyy-MM-dd";
	public static String DATE_TIME_FORMAT = "yyyyMMdd HH:mm:ss";
	public static String DATE_TIME_FORMAT_ = "yyyy-MM-dd HH:mm:ss";
	public static String TIME_FORMAT = "hh:mm:ss";

	/**
	 * 
	 * @Description：获取当前8位日期
	 * @return String
	 * @author QingXu
	 * @date 2018年11月1日 上午11:19:57
	 */
	public static String getCurrDate() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return df.format(new Date());
	}

	/**
	 * 
	 * @Description：获取当天17位时间
	 * @return String
	 * @author QingXu
	 * @date 2018年11月1日 上午11:21:35
	 */
	public static String getCurrDateTime() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
		return df.format(new Date());
	}

	/**
	 * 
	 * @Description：根据格式获取当前时间
	 * @param pattern
	 * @return String
	 * @author QingXu
	 * @date 2018年11月1日 上午11:33:32
	 */
	public static String getCurrDateTime(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date());
	}
	public static void main(String[] args) {
		System.out.println(getCurrDateTime());
	}
}
