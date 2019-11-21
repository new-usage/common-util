package com.yoonicoo.nu.common.util.time;/**
 * 
 * @description 日期格式模型
 * @author QingXu
 * @date 2019/11/21 11:33
 */
public enum FormatPatternEnum {

    DATE_FORMATE("yyyyMMdd"),
    DATE_FORMATE_HYPHEN("yyyy-MM-dd"),
    DATE_TIME_FORMAT("yyyyMMdd HH:mm:ss"),
    DATE_TIME_FORMAT_HYPHEN("yyyy-MM-dd HH:mm:ss"),
    TIME_FORMAT("HH:mm:ss");

    private String value;

    private FormatPatternEnum (String value) {
        this.value = value;
    }
    public String value() {
        return this.value;
    }
}
