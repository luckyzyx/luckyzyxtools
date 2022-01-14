package com.luckyzyx.tools.hook;

import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.utils.XSPUtils;

import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hooktest {

    public void hook(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        if (XSPUtils.getBoolean("hooktest",false)){
            hooktext(lpparam);
        }
    }
    public void hooktext(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class<?> clazz;
        clazz = lpparam.classLoader.loadClass("com.luckyzyx.test.MainActivity");
        XposedHelpers.findAndHookMethod(clazz, "istext", new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult("hook");
            }
        });
    }
}
