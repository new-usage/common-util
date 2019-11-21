package com.yoonicoo.nu.common.util;/**
 * 
 * @description 
 * @author QingXu
 * @date 2019/11/19 17:15
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String HYPHEN = "-";
    /**
     *
     * @Description：中划线转驼峰模式
     * @param key
     * @return String
     * @author QingXu
     * @date 2019年3月20日 下午6:04:01
     */
    public static String mapHyphenToCamelCase(String str) {
        String[] nameParts = str.split(HYPHEN);
        String ret = nameParts[0];
        if (nameParts.length == 1) return str;
        for (int i = 1; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            ret = ret.concat(namePart.substring(0, 1).toUpperCase().concat(namePart.substring(1)));
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(mapHyphenToCamelCase("max-abc"));
    }
}
