package com.luckyzyx.tools.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hookeast2d {
    public void hook(XC_LoadPackage.LoadPackageParam lpparam){
        XposedHelpers.findAndHookMethod("com.east2d.haoduo.ui.activity.SplashActivity",lpparam.classLoader, "isAdOpen", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity",lpparam.classLoader, "isVip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });
    }
}
