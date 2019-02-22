package com.example.hp.dsnotes;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class dataUtil {

    public static String DateFromLong(long time){

        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyy 'at' hh:mm aaa" , Locale.US);
        return dateFormat.format(new Date(time));

    }
}
