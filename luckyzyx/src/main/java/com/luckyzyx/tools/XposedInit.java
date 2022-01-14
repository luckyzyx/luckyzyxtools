package com.luckyzyx.tools;

import com.luckyzyx.tools.hook.hookeast2d;
import com.luckyzyx.tools.hook.hookpackageinstaller;
import com.luckyzyx.tools.utils.Log;

import android.content.SharedPreferences;

import com.luckyzyx.tools.hook.hooktest;

//当指定应用被加载时被调用，一般用于hook特定应用的方法
import de.robv.android.xposed.IXposedHookLoadPackage;
//在Android系统启动时被调用，作用于初始的zygote进程，可用于实现应用于所有应用的hook
import de.robv.android.xposed.IXposedHookZygoteInit;
//指定应用的资源进行初始化时被调用，一般用于资源的替换
import de.robv.android.xposed.IXposedHookInitPackageResources;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage,IXposedHookInitPackageResources {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        switch (lpparam.packageName) {
            case "com.luckyzyx.test":
                new hooktest().hook(lpparam);
                break;
            case "com.east2d.everyimage":
                new hookeast2d().hook(lpparam);
                break;
            case "com.android.packageinstaller":
                new hookpackageinstaller().hook(lpparam);
                break;
        }
    }

    //findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback)
    //className:被hook的方法所在类的完整名称，包括 包名 + 类名
    //classLoader:可通过 lpparam.classLoader 获得
    //methodName:被hook的方法的名，注意如有混淆，则应该用混淆后的名字
    //object... 与为被hook的方法的参数对应
    //Callback  ：指定该方法被调用时，需要被执行的回调

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        // similar to handleLoadPackage
    }

}
