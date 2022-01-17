package com.luckyzyx.tools.utils;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {
    private static final XSharedPreferences XSprefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "XposedSettings");
    private static final XSharedPreferences OSprefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "OtherSettings");
    private static final XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "Settings");

    public static boolean getBooleanXS(String prefsName, Boolean defValue){
        return XSprefs.getBoolean(prefsName, defValue);
    }
    public static boolean getBooleanOS(String prefsName, Boolean defValue){
        return OSprefs.getBoolean(prefsName, defValue);
    }
    public static boolean getBoolean(String prefsName, Boolean defValue){
        return prefs.getBoolean(prefsName, defValue);
    }
    public static String getString(String prefsName, String defValue){
        return prefs.getString(prefsName, defValue);
    }
}
