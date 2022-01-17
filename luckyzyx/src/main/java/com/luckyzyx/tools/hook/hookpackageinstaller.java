package com.luckyzyx.tools.hook;

import android.annotation.SuppressLint;

import com.luckyzyx.tools.utils.Log;
import com.luckyzyx.tools.utils.XSPUtils;

import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class hookpackageinstaller {
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        if (XSPUtils.getBooleanXS("safe_install",false)){
            hooksafe(lpparam);
        }
    }
    @SuppressLint("PrivateApi")
    public void hooksafe(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class<?> clazz;
        try{
            clazz = lpparam.classLoader.loadClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity");
            // Skip install guide跳过安装指南
            XposedHelpers.findAndHookMethod(clazz, "isStartAppDetail", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                }
            });
        }catch (Exception e){
            Log.d("safe","safe:"+e.getLocalizedMessage());
        }



    }
}
