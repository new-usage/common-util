package com.yoonicoo.nu.common.util.file;
/**
 * 
 * @Description：读取properties文件的几种方式
 * @author QingXu
 * @date 2019年3月15日 下午2:23:12
 */

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtils {

	private static Properties properties;

	/**
	 * @Description：第一种  Properties
	 * 1、 以 / 开头，则这样的路径是指定绝对路径，
	 * 2、一种不以 / 开头，则路径是相对与这个class所在的包的
	 */

	public static Properties getProperties(String name) throws Exception {

		InputStream is = Object.class.getResourceAsStream(name);
		properties = new Properties();
		properties.load(is);
		is.close();
		return properties;
	}
	/**
	 * 
	 * @Description：第二种 ResourceBundle
	 * 
	 * @param: ResourceBundle读取的文件路径是classpath下  例如com.yoonicoo.test.redis
	 * PS：name不加文件后缀
	 * 
	 */
	
	public static ResourceBundle getRourceBundle(String name) throws Exception {
		return ResourceBundle.getBundle(name); 
	}
}
