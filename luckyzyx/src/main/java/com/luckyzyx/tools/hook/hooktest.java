package com.luckyzyx.tools.hook;

import com.luckyzyx.tools.utils.Log;
import com.luckyzyx.tools.utils.XSPUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hooktest {

    private String tag = "hook test";
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        if (XSPUtils.getBooleanXS("hooktest",false)){
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
        XposedHelpers.findAndHookMethod("com.luckyzyx.test.MainActivity", lpparam.classLoader,
                "istext2", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Log.d(tag, (String) param.args[0]);
                        param.args[0] = "0";
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(tag, (String) param.args[0]);
                    }
                });
    }
}
