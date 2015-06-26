package com.miiskin.miiskin.Data;


import com.miiskin.miiskin.BuildConfig;

public final class L {
    public static String makeTag(Class cls) {
        return cls.getSimpleName();
    }

    private static final String TAG = "Miiskin";

    public static void v(String tag, String msg) {
        doV(tag, msg);
    }

    public static void v(String msg) {
        doV(TAG, msg);
    }

    public static void d(String tag, String msg) {
        doD(tag, msg);
    }

    public static void d(String msg) {
        doD(TAG, msg);
    }

    public static void i(String tag, String msg) {
        doI(tag, msg);
    }

    public static void i(String msg) {
        doI(TAG, msg);
    }

    public static void w(String msg) {
        doW(TAG, msg);
    }

    public static void w(Throwable t) {
        doW(TAG, t);
    }

    public static void e(String s) {
        doE(TAG, s);
    }

    public static void e(Throwable t) {
        doE(TAG, t);
    }

    public static void e(String m, Throwable t) {
        doE(TAG, m, t);
    }

    private static void doV(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    private static void doD(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    private static void doI(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    private static void doW(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    private static void doW(String tag, Throwable t) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, t);
        }
    }

    private static void doE(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }

    private static void doE(String tag, Throwable t) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, t.getMessage(), t);
        }
    }

    private static void doE(String tag, String msg, Throwable t) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg, t);
        }
    }


}
