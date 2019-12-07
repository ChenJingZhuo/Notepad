package com.cjz.notepad.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DBUtils {

    public static final String DATABASE_NAME="Notepad";
    public static final String DATABASE_TABLE="NOTE";
    public static final int DATABASE_VERSION=1;

    public static final String NOTEPAD_ID="id";
    public static final String NOTEPAD_CONTENT="content";
    public static final String NOTEPAD_TIME="notetime";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static final String getTime(){
        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        Log.d("DBUtils", ""+simpleDateFormat.format(date));*/

        /*Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String date_time=year+"年"+(month+1)+"月"+date+"日"+(hour+8)+"时"+minute+"分"+second+"秒";*/

        LocalDateTime dateTime=LocalDateTime.now();
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        Log.d("DBUtils", "dateTime.format(dtf)");
        return dateTime.format(dtf);
    }
}
