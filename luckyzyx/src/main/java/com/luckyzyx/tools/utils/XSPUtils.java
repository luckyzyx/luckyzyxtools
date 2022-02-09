package com.luckyzyx.tools.utils;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {
    private static final XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "XposedSettings");

    public static boolean getBoolean(String prefsName, Boolean defValue){
        return prefs.getBoolean(prefsName, defValue);
    }
    public static String getString(String prefsName, String defValue){
        return prefs.getString(prefsName, defValue);
    }
}
