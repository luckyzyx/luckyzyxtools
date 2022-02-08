package com.luckyzyx.tools.utils;

import com.luckyzyx.tools.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {
    private static final XSharedPreferences XSprefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "XposedSettings");
    private static final XSharedPreferences OSprefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "OtherSettings");

    public static boolean getBooleanXS(String prefsName, Boolean defValue){
        return XSprefs.getBoolean(prefsName, defValue);
    }
    public static String getStringXS(String prefsName, String defValue){
        return XSprefs.getString(prefsName, defValue);
    }
    public static boolean getBooleanOS(String prefsName, Boolean defValue){
        return OSprefs.getBoolean(prefsName, defValue);
    }
    public static String getStringOS(String prefsName, String defValue){
        return OSprefs.getString(prefsName, defValue);
    }
}
