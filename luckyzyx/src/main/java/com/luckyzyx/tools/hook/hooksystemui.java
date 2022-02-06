package com.luckyzyx.tools.hook;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hooksystemui {
//    private final String tag = "SystemUI";

    public void hooknetworkspeed(XC_LoadPackage.LoadPackageParam lpparam){
        XposedBridge.log("hook system start!");
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                XposedHelpers.findAndHookMethod("com.oplusos.systemui.statusbar.controller.NetworkSpeedController", lpparam.classLoader,
                        "postUpdateNetworkSpeedDelay", new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                XposedBridge.log("hook systemui : "+param.args[0]);
                            }
                        });
            }
        });
        XposedBridge.log("hook system stop!");
    }
}
