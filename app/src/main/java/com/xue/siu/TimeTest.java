package com.xue.siu;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XUE on 2016/1/29.
 */
public class TimeTest {

    public static void main(String args[]) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        System.out.println(format.format(date));
    }
}
