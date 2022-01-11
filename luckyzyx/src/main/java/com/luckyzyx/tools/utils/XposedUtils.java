package com.luckyzyx.tools.utils;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XposedUtils {
    //https://www.jianshu.com/p/c0e9a1baaa05
    private static final XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "luckyzyx");
    public static boolean getPrefs(String prefsName, Boolean defaultValue){
        return prefs.getBoolean(prefsName, defaultValue);
    }
}