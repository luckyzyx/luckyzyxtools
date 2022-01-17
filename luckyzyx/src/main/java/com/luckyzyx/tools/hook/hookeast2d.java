package com.luckyzyx.tools.hook;

import com.luckyzyx.tools.utils.XSPUtils;

import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hookeast2d {
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        if (XSPUtils.getBooleanXS("ad",false)) {
            HookisAdOpen(lpparam);
        }
        if (XSPUtils.getBooleanXS("vip",false)) {
            HookisVip(lpparam);
        }
    }

    public void HookisAdOpen(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.east2d.haoduo.ui.activity.SplashActivity", lpparam.classLoader, "isAdOpen", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });
    }

    public void HookisVip(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity", lpparam.classLoader, "isVip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });
    }


}
