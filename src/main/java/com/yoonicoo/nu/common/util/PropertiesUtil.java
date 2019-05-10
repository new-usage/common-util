package com.yoonicoo.nu.common.util;
/**
 * 
 * @Description：读取properties文件的几种方式
 * @ClassName： PropertiesUtil
 * @ProjectName：common-util-use
 * @Package：com.yoonicoo.nu.common.util
 * @author QingXu
 * @date 2019年3月15日 下午2:23:12
 */

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtil {

	private static Properties properties;

	/**
	 * @Description：第一种  Properties
	 * @param: 资源文件的路径从根目录 / 开始
	 * 
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
	 * @param 不加文件后缀
	 * 
	 */
	
	public static ResourceBundle getRourceBundle(String name) throws Exception {
		return ResourceBundle.getBundle(name); 
	}
}
