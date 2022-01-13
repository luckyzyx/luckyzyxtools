package com.luckyzyx.tools.utils;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {
    static XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "XposedSettings");

    public static boolean getBoolean(String keyName, boolean defValue) {
        return prefs.getBoolean(keyName,defValue);
    }
}
