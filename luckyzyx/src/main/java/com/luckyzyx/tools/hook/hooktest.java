package com.luckyzyx.tools.hook;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.luckyzyx.tools.XposedInit;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hooktest {
    public void hook(){
        XposedHelpers.findAndHookMethod(Application.class, "istext", new XC_MethodHook() {
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult("hook");
            }
        });
    }

}
