package com.se.utils;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: wtz
 * Date:2019/6/25
 */
public class DateUtils {

    //SimpleDateFormat 是线程不安全的类
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue(){
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String get(Date date){
        String format = df.get().format(date);
        return format;
    }

    @Test
    public void main() {
        System.out.println(get(new Date()));
    }
}
