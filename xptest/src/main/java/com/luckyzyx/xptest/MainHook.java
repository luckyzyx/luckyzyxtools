package com.luckyzyx.xptest;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (lpparam.packageName.equals("com.coloros.alarmclock")) {
            final Class<?> clazz = XposedHelpers.findClass("com.coloros.widget.smallweather.OnePlusWidget", lpparam.classLoader);

            XposedHelpers.setStaticObjectField(clazz,"Sc","");
        }
    }
}
