package com.yoonicoo.nu.common.util.time;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.*;
import java.util.Optional;
import java.util.Set;

/**
 * @author QingXu
 * @description 时间工具类
 * @date 2019/11/19 14:55
 */
@Slf4j
public class DateUtils {

    /**
     * get now()
     * @param LocalFormatTypeEnum format type
     * @param DateTimeTypeEnum LocalDate or LocalDateTime type
     */
    /*
    private static String getInnerCurrentLocalDateTime(FormatTypeEnum formatType, DateTimeTypeEnum timeType){
        TemporalAccessor temporalAccessor;
        switch (timeType) {
            case DATE:
                temporalAccessor = LocalDate.now();
                break;
            case DATETIME:
                temporalAccessor = LocalDateTime.now();
                break;
            default:
                throw new IllegalArgumentException("illegal dateTimeType: " + timeType);
        }
        return DateTimeFormatter.ofPattern(formatType.value()).format(temporalAccessor);
    }*/

    /**
     * @param FormatPatternEnum formatType
     * @return 返回当前系统日期
     */
    public static String getCurrentDate(FormatPatternEnum formatType) {
        return DateTimeFormatter.ofPattern(FormatPatternEnum.DATE_FORMATE.value()).format(LocalDate.now());
    }

    /**
     * @return 返回当前系统日期 formate yyyyMMdd
     */
    public static String getCurrentDate() {
        return getCurrentDate(FormatPatternEnum.DATE_FORMATE);
    }

    /**
     * @return 返回当前系统日期 formate yyyy-MM-dd
     */
    public String getCurrentDateHyphen() {
        return getCurrentDateTime(FormatPatternEnum.DATE_FORMATE_HYPHEN);
    }


    /**
     * @param FormatPatternEnum formatType
     * @return 返回当前系统日期+时间
     */
    public static String getCurrentDateTime(FormatPatternEnum formatType) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatPatternEnum.DATE_TIME_FORMAT.value()));
    }

    /**
     * @return 返回当前系统日期 formate yyyyMMdd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime(FormatPatternEnum.DATE_TIME_FORMAT);
    }

    /**
     * @return 返回当前系统日期 formate yyyy-MM-dd HH:mm:ss
     */
    public String getCurrentDateTimeHyphen() {
        return getCurrentDateTime(FormatPatternEnum.DATE_TIME_FORMAT_HYPHEN);
    }

    /**
     * 获取两个日期之间的天数
     */
    public static int getBetweenDays(String startDate, String endDate, FormatPatternEnum formatPattern) {
        long startDays = queryLocalDate(startDate, formatPattern).toEpochDay();
        long endDays = queryLocalDate(endDate, formatPattern).toEpochDay();
        int days = (int) (endDays - startDays);
        return Math.abs(days);
    }
    /**
     * 获取两个日期之间的月数
     */
    public static int getBetweenMonths(String startDate, String endDate, FormatPatternEnum formatPattern) {
        LocalDate start = queryLocalDate(startDate, formatPattern);
        LocalDate end = queryLocalDate(endDate, formatPattern);
        Period period = Period.between(start, end);
        int months = period.getYears() * 12 + period.getMonths();
        return Math.abs(months);
    }

    /**
     * 获取LocalDate
     */
    private static LocalDate queryLocalDate(String date, FormatPatternEnum formatPattern) {
        try {
            return DateTimeFormatter.ofPattern(formatPattern.value()).parse(date).query(TemporalQueries.localDate());
        } catch (DateTimeParseException ex) {
            log.error("String date '" + date + "' could not be parsed: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * 验证字符串是否是有效的日期
     */
    public static boolean isValidDate(String date, FormatPatternEnum formatPattern) {
        try {
            DateTimeFormatter.ofPattern(formatPattern.value()).parse(date);
        } catch (DateTimeParseException ex) {
            log.error("String date '" + date + "' could not be parsed: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
