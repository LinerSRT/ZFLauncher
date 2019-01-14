package com.rmicro.launcher.utils;

import android.util.Log;

/**
 * Created by Tim on 2017/12/11.
 */

public class LogUtil {

    public static boolean isDebug = true;
    public static String TAG = "[[ YANWEI-Launcher ]]/";

    private LogUtil() {
    }

    public static void v(String tag, String msg) {
        if(isDebug) {
            Log.v(TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if(isDebug) {
            Log.v(TAG + tag, msg, tr);
        }

    }

    public static void d(String tag, String msg) {
        if(isDebug) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(isDebug) {
            Log.d(TAG + tag, msg, tr);
        }

    }

    public static void i(String tag, String msg) {
        if(isDebug) {
            Log.i(TAG + tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if(isDebug) {
            Log.i(TAG + tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if(isDebug) {
            Log.w(TAG + tag, msg);
        }
    }

    public static void w(String tag, Throwable tr) {
        if(isDebug) {
            Log.w(TAG + tag, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if(isDebug) {
            Log.w(TAG + tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if(isDebug) {
            Log.e(TAG + tag, msg);
        }
    }
    public static void e(String tag, String msg, Throwable tr) {
        if(isDebug) {
            Log.e(TAG + tag, msg, tr);
        }
    }
}
