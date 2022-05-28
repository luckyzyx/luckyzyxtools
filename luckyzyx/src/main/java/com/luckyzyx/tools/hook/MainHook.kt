package com.luckyzyx.tools.hook

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.luckyzyx.tools.hook.CorePatch.CorePatchForR
import com.luckyzyx.tools.hook.CorePatch.CorePatchForS
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {

    @Suppress("unused")
    private val PrefsFile = "XposedSettings"

    override fun onInit() {
        configs {
            // 全局调试用的 TAG,在 Logcat 控制台过滤此 TAG 可找到详细日志
            debugTag = "YukiHook"
            // 是否开启调试模式,请注意 - 若作为发布版本请务必关闭调试功能防止对用户设备造成大量日志填充
            isDebug = false
            // 是否启用调试日志的输出功能,一旦关闭后除手动日志外 API 将停止全部日志的输出 - 建议不要随意关掉这个选项
            // 虽然说对用户的设备写入大量日志是不正确的 - 但是没有日志你将无法调试
            // 关于日志是否会影响设备的流畅度一直是一个伪命题,但是不设置这个选项可能会引起一些非议 - 建议不要关闭就是了
            isAllowPrintingLogs = true
            // 是否启用 [YukiHookModulePrefs] 的键值缓存功能
            // 若无和模块频繁交互数据在宿主重新启动之前建议开启,若需要实时交互数据建议关闭或从 [YukiHookModulePrefs] 中进行动态配置
            isEnableModulePrefsCache = true
            // 是否启用 [Member] 缓存功能
            // 为防止 [Member] 复用过高造成的系统 GC 问题 - 此功能默认启用
            // 除非缓存的 [Member] 发生了混淆的问题 - 否则建议启用
            isEnableMemberCache = true
        }
    }

    override fun onHook() = encase {
        //系统框架
        loadSystem(HookAndroid())
        //系统界面
        loadApp("com.android.systemui",HookSystemUI())
        //设置
        //loadApp("com.android.settings"){}
        //系统桌面
        loadApp("com.android.launcher",HookLauncher())
        //时钟
        loadApp("com.coloros.alarmclock",HookAlarmClock())
        //应用包安装程序
        loadApp("com.android.packageinstaller",HookPackageInstaller())
        //好多动漫
        loadApp("com.east2d.everyimage",HookMoreAnime())
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onInitZygote { startupParam: IXposedHookZygoteInit.StartupParam ->
            run {
                when(SDK_INT){
                    30 -> CorePatchForR().initZygote(startupParam)
                    31 -> CorePatchForS().initZygote(startupParam)
                    else -> XposedBridge.log("[CorePatch] 不支持的Android版本: $SDK_INT")
                }
            }
        }
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                if ("android" == lpparam.packageName && lpparam.processName == "android") {
                    when(SDK_INT) {
                        30 -> CorePatchForR().handleLoadPackage(lpparam)
                        31 -> CorePatchForS().handleLoadPackage(lpparam)
                        else -> XposedBridge.log("[CorePatch] 不支持的Android版本: $SDK_INT")
                    }
                }
            }
        }
    }
}