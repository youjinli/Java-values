/**
 * Copyright 2016 Welab, Inc. All rights reserved.
 * WELAB PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.values.utils;

import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author <a href="mailto:cyberyang@wolaidai.com">cyberyang</a>
 */
abstract public class DateUtils {
    public static final String GMT_EIGHT_TIMEZONE = "GMT+8";
    public static final int UTC_OFFSET_IN_HOURS = calculateUtcOffsetInHours();
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    private static final String TIME_STAMP_FORMAT = "yyMMddHHmmssSSS";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String  YYYYMMDD = "yyyyMMdd";
    
    public static final String DATE_YYYYMM_FORMAT = "yyyy-MM";

    private static int calculateUtcOffsetInHours() {
        int timeZoneHoursInMills = TimeZone.getTimeZone(ZoneId.systemDefault()).getRawOffset()
                - TimeZone.getTimeZone(ZoneOffset.UTC).getRawOffset();
        long oneHoursInMills = ChronoUnit.HOURS.getDuration().toMillis();

        return (int) (timeZoneHoursInMills / oneHoursInMills);
    }

    ;

    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getTimeSeconds(Date date) {
        return date.getTime() / 1000L;
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static int getCurrentHour() {
        return LocalTime.now(ZoneId.systemDefault()).getHour();
    }

    public static Date getCurrentDate() {
        return localDate2Date(LocalDate.now());
    }

    public static String parseDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_STAMP_FORMAT);
        return dateFormat.format(date);
    }

    public static String parseDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * Please DO NOT make the modifier of this method to PUBLIC
     *
     * @param date
     * @return
     */
    private static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Please DO NOT make the modifier of this method to PUBLIC
     *
     * @param date
     * @return
     */
    private static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Please DO NOT make the modifier of this method to PUBLIC
     *
     * @param localDate
     * @return
     */
    private static Date localDate2Date(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    /**
     * 获取2个时间的相差月数
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public static int getMonthDiff(Date start, Date end){

               return calDiffMonth(start,end);

    }


    /**
     * Going(Went) to some time from now(DateTime),
     *
     * @param offSet To some time in future if positive value, to some time ago otherwise.
     * @param unit
     * @return
     */
    public static Date travelTo(int offSet, Unit unit) {
        return travelTo(getCurrentDateTime(), offSet, unit);
    }

    public static Date travelTo(Date targetDate, int offSet, Unit unit) {
        return Date.from(targetDate.toInstant().plus(unit.value().multipliedBy(offSet)));
    }

    /**
     * Calculates the amount of days until the specified time.
     * <p>
     * {@link Instant#until}.
     *
     * @param text The text must represent a valid instant in UTC and is parsed using
     *        {@link DateTimeFormatter#ISO_INSTANT}. Such as {@code 2007-12-03T10:15:30.00Z}.
     * @return
     */
    public static long daysUtilNow(String text) {
        return Instant.now().until(Instant.parse(text), ChronoUnit.DAYS);
    }

    /**
     * Calculates the amount of days until the specified Date.
     * <p>
     * Specific date to current date means using current date subtracts the date. If the result is
     * positive, means specific date is the date before current date, otherwise in future.
     * <p>
     * {@link Instant#until}.
     *
     * @param date
     * @return
     */
    public static long daysUntilCurrentDate(Date date) {
        return date2LocalDate(date).until(LocalDate.now(), ChronoUnit.DAYS);
    }

    public static long daysUntilCurrentDate(java.sql.Date date) {
        return date.toLocalDate().until(LocalDate.now(), ChronoUnit.DAYS);
    }

    public static boolean isAfterCurrentDate(Date date) {
        return date2LocalDate(date).isAfter(LocalDate.now());
    }

    public static boolean isAfterCurrentDate(java.sql.Date date) {
        return date.toLocalDate().isAfter(LocalDate.now());
    }

    public static boolean isAfterCurrentDateTime(Date date) {
        return date2LocalDateTime(date).isAfter(LocalDateTime.now());
    }

    public static boolean isAfter(Date a, Date b) {
        LocalDate locaDateA = date2LocalDate(a);
        LocalDate locaDateB = date2LocalDate(b);
        return locaDateA.isAfter(locaDateB);
    }

    public static boolean isAfter(java.sql.Date a, Date b) {
        LocalDate locaDateA = a.toLocalDate();
        LocalDate locaDateB = date2LocalDate(b);
        return locaDateA.isAfter(locaDateB);
    }

    public static boolean isDateTimeAfter(Date a, Date b) {
        return date2LocalDateTime(a).isAfter(date2LocalDateTime(b));
    }

    public static boolean isBeforeCurrentDate(Date date) {
        return date2LocalDate(date).isBefore(LocalDate.now());
    }

    public static boolean isBeforeCurrentDate(java.sql.Date date) {
        return date.toLocalDate().isBefore(LocalDate.now());
    }

    public static boolean isBeforeCurrentDateTime(Date date) {
        return date2LocalDateTime(date).isBefore(LocalDateTime.now());
    }

    public static boolean isBefore(Date a, Date b) {
        LocalDate locaDateA = date2LocalDate(a);
        LocalDate locaDateB = date2LocalDate(b);
        return locaDateA.isBefore(locaDateB);
    }

    public static boolean isBefore(java.sql.Date a, Date b) {
        LocalDate locaDateA = a.toLocalDate();
        LocalDate locaDateB = date2LocalDate(b);
        return locaDateA.isBefore(locaDateB);
    }

    public static boolean isDateTimeBefore(Date a, Date b) {
        return date2LocalDateTime(a).isBefore(date2LocalDateTime(b));
    }

    public static boolean isCurrentDateTimeBetween(Date a, Date b) {
        return isBeforeCurrentDateTime(a) && isAfterCurrentDateTime(b);
    }

    public static boolean isDateTimeBetween(Date target, Date a, Date b) {
        return isDateTimeBefore(a, target) && isDateTimeAfter(b, target);
    }

    public static boolean isEqualsCurrentDate(Date date) {
        return LocalDate.now().isEqual(date2LocalDate(date));
    }

    public static boolean isEqual(Date a, Date b) {
        LocalDate locaDateA = date2LocalDate(a);
        LocalDate locaDateB = date2LocalDate(b);
        return locaDateA.isEqual(locaDateB);
    }

    public static DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    public static DateFormat getChinaLongDateFormat() {
        return DateFormat.getDateInstance(DateFormat.LONG, Locale.CHINA);
    }

    public static int getYear(Date date) {
        return date2LocalDate(date).getYear();
    }

    public static int getMonth(Date date) {
        return date2LocalDate(date).getMonthValue();
    }

    /**
     * DurationInDays = StartDate - EndDate
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int durationInDays(Date startDate, Date endDate) {
        return (int) endDate.toInstant().until(startDate.toInstant(), ChronoUnit.DAYS);
    }

    /**
     * Days can be negative value, if so, the value means get the date before target date.
     *
     * @param targetDate
     * @param days
     * @return
     */
    public static Date plusDays(Date targetDate, int days) {
        return Date.from(targetDate.toInstant().plus(days, ChronoUnit.DAYS));
    }

    public static Date plusMinutes(Date targetDate, int minutes) {
        return Date.from(targetDate.toInstant().plus(minutes, ChronoUnit.MINUTES));
    }

    /**
     * Days can be negative value, if passing negative value, it means travel to the date before
     * current date.
     *
     * @param days
     * @return
     */
    public static Date plusDaysOnNow(int days) {
        return plusDays(getCurrentDateTime(), days);
    }

    public static Date plusMinutesOnNow(int minutes) {
        return plusMinutes(getCurrentDateTime(), minutes);
    }

    /**
     * Plus days on current date, without time.
     *
     * @param days
     * @return
     */
    public static Date plusDaysOnCurrentDate(int days) {
        return plusDays(getCurrentDate(), days);
    }

    /**
     * Months can be negative value, if passing negative value, it means travel to the date before
     * current date.
     *
     * @param months
     * @return
     */
    public static Date plusNaturalMonthOnCurrentDate(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Check if targetDate is specific hours ago.
     *
     * @param targetDate
     * @param hours
     * @return
     */
    public static boolean isHoursAgo(Date targetDate, int hours) {
        Instant hoursAgoInstant = Instant.now().minus(hours, ChronoUnit.HOURS);
        return targetDate.toInstant().isBefore(hoursAgoInstant);
    }

    public static boolean isOutBussinessTime() {
        return getCurrentHour() >= 0 && getCurrentHour() <= 6;
    }

    public static java.sql.Date date2SqlDate(Date date) {
        java.sql.Date result = new java.sql.Date(date.getTime());
        return result;
    }

    /**
     * CAUTION: This method ONLY be use WHEN INSERT DATE/TIME TO database.
     *
     * @param unadjustDate
     * @return
     */
    public static Date adjustTimeToSuitUTCTimeZone(Date unadjustDate) {
        return Date.from(unadjustDate.toInstant().plus(UTC_OFFSET_IN_HOURS, ChronoUnit.HOURS));
    }

    public static Date getCurrentDateWithoutTime() {
        return getDateWithoutTime(getCurrentDate());
    }

    public static Date getDateWithoutTime(Date date) {
        return org.apache.commons.lang.time.DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }

    public static Date parseDate(String stringDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(stringDate);
        } catch (ParseException e) {
            LOGGER.error("DateUtils.parseDate error", e);
        }

        return null;
    }

    public static boolean isTimeBefore(Date a, Date b) {
        LocalDateTime locaDateTimeA = date2LocalDateTime(a);
        LocalDateTime locaDatetimeB = date2LocalDateTime(b);
        return locaDateTimeA.isBefore(locaDatetimeB);
    }

    public static String getDay(Date date) {
        Calendar calendar = getGregorianCalendar(date);
        return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Date addDay(Date date, Integer month) {
        Calendar calendar = getGregorianCalendar(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 初始化日期类，date为空，取当前日期
     *
     * @param date
     * @return
     */
    private static Calendar getGregorianCalendar(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        return calendar;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            LOGGER.error("DateUtils.parseDate error", e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * date是否在start和end中间
     *
     * @param start
     * @param end
     * @param date
     * @return
     */
    public static boolean compareDate(Date start, Date end, Date date) {
        if (date.getTime() >= start.getTime() && date.getTime() <= end.getTime()) {
            return true;
        }
        return false;
    }

    public static enum Unit {
        DAYS(ChronoUnit.DAYS.getDuration()), HOURS(ChronoUnit.HOURS.getDuration()), MINUTES(
                ChronoUnit.MINUTES.getDuration()), SECONDS(ChronoUnit.SECONDS.getDuration());
        private Duration m_duration;

        private Unit(Duration duration) {
            m_duration = duration;
        }

        public Duration value() {
            return m_duration;
        }
    }




    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public static int calDiffMonth(Date start,Date end){
        int result=0;
            int startYear=getYear(start);
            int startMonth=getMonth(start);
            int startDay=Integer.valueOf(getDay(start));
            int endYear=getYear(end);
            int endMonth=getMonth(end);
            int endDay=Integer.valueOf(getDay(end));
            if (startDay>endDay){ //1月17  大于 2月28
                if (endDay==getDaysOfMonth(getYear(new Date()),2)){   //也满足一月
                    result=(endYear-startYear)*12+endMonth-startMonth;
                }else{
                    result=(endYear-startYear)*12+endMonth-startMonth-1;
                }
            }else{
                result=(endYear-startYear)*12+endMonth-startMonth;
            }
        return result;
    }
}
