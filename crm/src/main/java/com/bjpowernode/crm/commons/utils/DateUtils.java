package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description: 日期工具类
 *
 * @author: WGQ
 * @date: 2022.12.03
 * @email: wgqcn@foxmail.com
 */
public class DateUtils {
    /**
     * 对指定date对象进行格式化: yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formartDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    /**
     * 对指定date对象进行格式化: yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formartDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    /**
     * 对指定date对象进行格式化: HH:mm:ss
     * @param date
     * @return
     */
    public static String formartTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

}
