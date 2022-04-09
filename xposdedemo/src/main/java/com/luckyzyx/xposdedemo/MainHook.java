package com.luckyzyx.xposdedemo;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if(!(lpparam == null)){
            XposedBridge.log("[原生XP测试package]"+lpparam.appInfo.packageName);
            XposedBridge.log("[原生XP测试appinfo]"+lpparam.appInfo.toString());
        }else{
            XposedBridge.log("[原生XP测试]  lpparam为null");
        }
    }
}