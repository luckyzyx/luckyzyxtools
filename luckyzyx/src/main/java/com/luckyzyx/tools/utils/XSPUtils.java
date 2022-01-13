package com.luckyzyx.tools.utils;

import android.content.SharedPreferences;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {
    private static final XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "XposedSettings");
    public static boolean getBoolean(String keyName, boolean defValue) {
        return prefs.getBoolean(keyName,defValue);
    }
}
