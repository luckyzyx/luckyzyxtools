package com.luckyzyx.tools.hook;

import com.luckyzyx.tools.utils.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hooksystemui {
    private final String tag = "System UI";

    public void hooknetworkspeed(XC_LoadPackage.LoadPackageParam lpparam){

    XposedHelpers.findAndHookMethod("com.oplusos.systemui.statusbar.controller.NetworkSpeedController", lpparam.classLoader,
            "postUpdateNetworkSpeedDelay",long.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.log(tag, (String) param.args[0]);
                }
            });
    }
}
