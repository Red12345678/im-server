package com.yk.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static String     dateFormatString = "yyyy-MM-dd HH:mm:ss";
    private static DateFormat dateFormat       = new SimpleDateFormat(dateFormatString);

    /**
     * [描述： 时间差计算]
     *
     * @param l Long
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/4/8
     */
    public static String timeDifference(Long l) {
        Long day  = l / (86400 * 1000);
        Long hour = (l / (3600 * 1000) - day * 24);
        Long min  = ((l / (60 * 1000)) - day * 1440 - hour * 60);
        Long sec  = ((l / 1000) - day * 86400 - hour * 3600 - min * 60);
        return String.format("%02d:%02d:%02d", hour.intValue(), min.intValue(), sec.intValue());
    }


    /**
     * [描述： 时间格式化去掉时间后面的.0]
     *
     * @param time String
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/5/7
     */
    public static String formatTime(String time) {

        try {
            if (time == null || time.equals("")) return null;
            return dateFormat.format(dateFormat.parse(time));
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * [描述： 得到当前时间通过格式]
     *
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/4/8
     */
    public static String getCurrentDateString() {
        return dateFormat.format(new Date());
    }

    /**
     * [描述： 计算时长]
     *
     * @param s1 String
     * @param s2 String
     * @return Long
     * @author yangkun[Email:vectormail@163.com] 2018/4/8
     */
    public static Long calHowTime(String s1, String s2) {
        try {
            Date d1 = dateFormat.parse(getCurrentDateString());
            if (s2 != null) {
                d1 = dateFormat.parse(s2);
            }
            if (s1 == null)
                s1 = getCurrentDateString();
            Date d2 = dateFormat.parse(s1);
            return d1.getTime() - d2.getTime();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String getDateFormatString() {
        return dateFormatString;
    }

    public static void setDateFormatString(String dateFormatString) {
        TimeUtil.dateFormatString = dateFormatString;
    }

    public static DateFormat getDateFormat() {
        return TimeUtil.dateFormat;
    }

    public static void setDateFormat(DateFormat dateFormat) {
        TimeUtil.dateFormat = dateFormat;
    }
}
