package com.iblogstreet.exerciselib.utils;

import android.util.Log;

public class Loger {
    public static final boolean SHOW_LOG_INFO = true;

    public static void e(String tag, String log) {
        if (SHOW_LOG_INFO) Log.e(tag, log);
    }

    public static void i(String tag, String log) {
        if (SHOW_LOG_INFO) Log.i(tag, log);
    }

    public static void d(String tag, String log) {
        if (SHOW_LOG_INFO) Log.d(tag, log);
    }

    public static void v(String tag, String log) {
        if (SHOW_LOG_INFO) Log.v(tag, log);
    }

    public static void w(String tag, String log) {
        if (SHOW_LOG_INFO) Log.w(tag, log);
    }


}

