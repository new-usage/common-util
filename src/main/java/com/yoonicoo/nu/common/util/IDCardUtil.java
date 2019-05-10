package com.yoonicoo.nu.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDCardUtil {

	private static Pattern pattern = Pattern.compile("(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)");
	/**
	 * 
	 * @Description：根据身份证解析性别
	 * @param id
	 * @return IdentityCard
	 * @author QingXu
	 * @date 2018年12月14日 下午12:17:54
	 */
	public static String parseSex(String id) {
		validateID(id);
		String sex = "";
		if (id.length() == 18) {
			if (Integer.parseInt(id.substring(16).substring(0, 1)) % 2 == 0) {
				sex = Sex.FEMALE.name();
			} else {
				sex = Sex.MALE.name();
			}
		}else {
			if (Integer.parseInt(id.substring(14, 15)) % 2 == 0) {
				sex = Sex.FEMALE.name();
	        } else {
	        	sex = Sex.MALE.name();
	        }
		}
		return sex;
	}
	/**
	 * 
	 * @Description：解析出生日期
	 * @param id
	 * @return String
	 * @author QingXu
	 * @date 2018年12月14日 下午12:47:13
	 */
	public static String parseBirthday(String id) {
		validateID(id);
		String birthday = "";
		if (id.length() == 18) {
			birthday = id.substring(6, 14);
		}else {
			birthday = "19"+id.substring(6, 12);
		}
		return birthday;
	}
	/**
	 * 
	 * @Description：解析年龄
	 * @param id
	 * @return String
	 * @author QingXu
	 * @date 2018年12月14日 下午12:47:26
	 */
	public static int parseAge(String id) {
		validateID(id);
		String birthday = parseBirthday(id);
	    LocalDate birDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyyMMdd").parse(birthday));
	    long age = ChronoUnit.YEARS.between(birDate, LocalDate.now());
		return (int)age;
	}
	/**
	 * 
	 * @Description：验证身份证
	 * @param id void
	 * @author QingXu
	 * @date 2018年12月14日 下午12:34:14
	 */
	private static void validateID(String id) {
		String exceptionMsg = Objects.requireNonNull(id, "身份证号不能为空");
		if (Objects.equals(id, "")) {
			throw new IllegalArgumentException(exceptionMsg);
		}
		Matcher matcher = pattern.matcher(id);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("身份证号码不合法");
		}
	}

	static enum Sex {
		MALE, FEMALE;
	}
	
	public static void main(String[] args) {
		String id = "511423201512140321"; //15位
		System.out.println(parseSex(id));
		System.out.println(parseBirthday(id));
		System.out.println(parseAge(id));
	}
}
