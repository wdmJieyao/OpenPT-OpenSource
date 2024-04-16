package fun.hzaw.commonbean.date;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * 新旧时间转换、时间戳转换工具类
 * 1.DATE转LOCALDATETIME
 * 2.DATE转LOCALDATE
 * 3.时间戳与LOCALDATE、LOCALDATETIME互相转换
 * <p>
 * 一些常见工具，避免重复造轮子
 * 虽然JDK8+以上封装了很多好用的通用方法，但是还是有些常见的没有实现，以下以<a href="https://www.hutool.cn/docs/#/core/%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4/%E6%A6%82%E8%BF%B0">htool</a>工具类里面常见的函数
 * LocalDateTimeUtil：日期转换、日期解析、格式化、偏移计算、间隔计算、获取一天的开始结束等。
 * ChineseDate（农历日期工具类）：农历日期，提供了生肖、天干地支、传统节日等方法。
 * </p>
 */
public class DateTimeConvertUtils {

    // 推荐创建不可变静态类成员变量
    private static final Log log = LogFactory.get();

    private DateTimeConvertUtils() {
    }

    /**
     * of("Asia/Shanghai") 默认时区
     */
    private final static ZoneId defaultZoneId = ZoneId.systemDefault();

    /**
     * Date转LocalDate
     *
     * @param fromDate
     * @param zoneId
     * @return
     */
    public static LocalDate getLocalDateFromDate(Date fromDate, ZoneId zoneId) {
        Objects.requireNonNull(fromDate, "fromDate can be not null");
        Objects.requireNonNull(zoneId, "zoneId can be not null");

        try {
            return fromDate.toInstant()
                    .atZone(zoneId)
                    .toLocalDate();
        } catch (Exception e) {
            log.error("getLocalDateFromDate..fromDate:{}..zoneId:{}..error:{}", fromDate, zoneId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static LocalDate getLocalDateFromDate(Date fromDate) {
        Objects.requireNonNull(fromDate, "fromDate can be not null");

        try {
            return fromDate.toInstant()
                    .atZone(defaultZoneId)
                    .toLocalDate();
        } catch (Exception e) {
            log.error("getLocalDateFromDate..fromDate:{}..error:{}", fromDate, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * date转Localdatetime
     *
     * @param fromDate
     * @param zoneId
     * @return
     */
    public static LocalDateTime getLocalDateTimeFromDate(Date fromDate, ZoneId zoneId) {
        Objects.requireNonNull(fromDate, "fromDate can be not null");
        Objects.requireNonNull(zoneId, "zoneId can be not null");

        try {
            return fromDate.toInstant()
                    .atZone(zoneId)
                    .toLocalDateTime();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromDate:{}..zoneId:{}..error:{}", fromDate, zoneId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime getLocalDateTimeFromDate(Date fromDate) {
        Objects.requireNonNull(fromDate, "fromDate can be not null");

        try {
            return fromDate.toInstant()
                    .atZone(defaultZoneId)
                    .toLocalDateTime();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromDate:{}..error:{}", fromDate, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * localDateTime转timestamp
     *
     * @param fromLDT
     * @return
     */
    public static long getTimestampFromLDT(LocalDateTime fromLDT) {
        Objects.requireNonNull(fromLDT, "fromLDT can be not null!");

        try {
            return fromLDT.atZone(defaultZoneId)
                    .toInstant()
                    .toEpochMilli();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromLDT:{}..error:{}", fromLDT, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * localDateTime转timestamp
     *
     * @param fromLDT
     * @return
     */
    public static long getTimestampFromLDT(LocalDateTime fromLDT, ZoneId zoneId) {
        Objects.requireNonNull(fromLDT, "fromLDT can be not null!");
        Objects.requireNonNull(zoneId, "zoneId can be not null!");

        try {
            return fromLDT.atZone(zoneId)
                    .toInstant()
                    .toEpochMilli();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromLDT:{}..error:{}", fromLDT, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * localDate转timestamp
     *
     * @param fromLD
     * @return
     */
    public static long getTimestampFromLD(LocalDate fromLD) {
        Objects.requireNonNull(fromLD, "fromLDT can be not null!");

        try {
            return fromLD.atStartOfDay(defaultZoneId)
                    .toInstant()
                    .toEpochMilli();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromLDT:{}..error:{}", fromLD, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * localDate转timestamp
     *
     * @param fromLD
     * @return
     */
    public static long getTimestampFromLD(LocalDate fromLD, ZoneId zoneId) {
        Objects.requireNonNull(fromLD, "fromLDT can be not null!");
        Objects.requireNonNull(zoneId, "zoneId can be not null!");

        try {
            return fromLD.atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli();
        } catch (Exception e) {
            log.error("getLocalDateTimeFromDate..fromLDT:{}..error:{}", fromLD, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 毫秒级时间戳转LDT
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        try {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), defaultZoneId);
        } catch (Exception e) {
            log.error("getDateTimeFromTimestamp..timestamp:{}..error:{}", timestamp, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 毫秒级时间戳转LD
     *
     * @param timestamp
     * @return
     */
    public static LocalDate getDateFromTimestamp(long timestamp) {
        try {
            return getDateTimeFromTimestamp(timestamp).toLocalDate();
        } catch (Exception e) {
            log.error("getDateFromTimestamp..timestamp:{}..error:{}", timestamp, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // public static void main(String[] args) {
    //     // Date转LocalDate
    //     // LocalDate localDate = getLocalDateFromDate(new Date());
    //     // System.out.println(localDate.format(dtf_date));
    //     //
    //     // LocalDateTime localDateTime = getLocalDateTimeFromDate(new Date());
    //     // System.out.println(localDateTime.format(dtf_date_time));
    //     // // 毫秒级时间戳
    //     // long timestamp = getTimestampFromLDT(LocalDateTime.now());
    //     // System.out.println("时间戳timestamp:" + timestamp);
    //     //
    //     // System.out.println(getTimestampFromLD(LocalDate.now()));
    //     // 1694669967000
    //
    //     System.out.println(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    // }

}
