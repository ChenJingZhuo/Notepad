package com.cjz.notepad.utils;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
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
        //api>=26
        /*LocalDateTime dateTime=LocalDateTime.now();
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        Log.d("DBUtils", "dateTime.format(dtf)");
        dateTime.format(dtf);*/

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return format.format(new Date());
    }
}
