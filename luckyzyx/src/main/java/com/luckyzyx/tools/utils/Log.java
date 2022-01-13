package com.luckyzyx.tools.utils;

import de.robv.android.xposed.XposedBridge;

public class Log {

    public static void d(String tag, String log){
        XposedBridge.log("["+tag+"]-"+log);
    }
}
