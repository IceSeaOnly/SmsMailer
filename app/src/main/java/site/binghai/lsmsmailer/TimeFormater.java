package site.binghai.lsmsmailer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/10.
 */
public class TimeFormater {
    public static String format(Long t){
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        return df.format(new Date(t));
    }

    public static String format_hh_mm(Date date){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }
    public static String format_hh_mm(Long date){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(new Date(date));
    }

    public static String getSuitableToday() {
        return getSuitableDateTime(System.currentTimeMillis());
    }
    public static String getSuitableDateTime(Long postTime) {

        String today = formatYYYYMMdd(System.currentTimeMillis());
        if(today.equals(formatYYYYMMdd(postTime)))
            return "今天 "+format_hh_mm(postTime);

        int days = (int) Math.abs((System.currentTimeMillis()-postTime)
                / (24 * 60 * 60 * 1000)) + 1;

        if(days == 1)
            return "昨天 "+format_hh_mm(postTime);

        if(days<5)
            return days+" 天前 "+format_hh_mm(postTime);
        return format(postTime);
    }

    private static String formatYYYYMMdd(Long td) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(td));
    }

    public static String format_dd(Long td) {
        SimpleDateFormat df = new SimpleDateFormat("dd");
        return df.format(new Date(td));
    }

    public static String format_Month(Long postTime) {
        String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        SimpleDateFormat df = new SimpleDateFormat("MM");
        return months[Integer.parseInt(df.format(postTime))-1];
    }
}
