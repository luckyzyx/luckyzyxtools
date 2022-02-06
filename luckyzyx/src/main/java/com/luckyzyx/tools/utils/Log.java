package com.luckyzyx.tools.utils;

import android.content.Context;
import android.widget.Toast;

import de.robv.android.xposed.XposedBridge;

public class Log {

    public static void d(String tag, String log){
        XposedBridge.log("["+tag+"]-"+log);
    }

    public static void toast(Context context, String tag, String log){
        Toast.makeText(context, "["+tag+"]-"+log, Toast.LENGTH_SHORT).show();
    }
}
