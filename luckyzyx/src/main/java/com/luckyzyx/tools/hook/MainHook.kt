package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.YukiHookXposedInitProxy

@InjectYukiHookWithXposed
class MainHook : YukiHookXposedInitProxy {

    private val PrefsFile = "XposedSettings"

    override fun onInit() {
        configs {
            // 全局调试用的 TAG
            debugTag = "YukiHook"
            // 是否开启调试模式,开启后模块将会向 Logcat 和 XposedBridge.log 打印详细的 Hook 日志，关闭后仅会打印 E 级别的日志
            isDebug = false
            // 是否启用调试日志的输出功能,关闭后将会停用 YukiHookAPI 对全部日志的输出.但是不影响当你手动调用日志方法输出日志
            isAllowPrintingLogs = true
            // 是否启用 [YukiHookModulePrefs] 的键值缓存功能
            // 若无和模块频繁交互数据在宿主重新启动之前建议开启
            // 若需要实时交互数据建议关闭或从 [YukiHookModulePrefs] 中进行动态配置
            isEnableModulePrefsCache = true
        }
    }

    override fun onHook() {
        encase {
            if (prefs(PrefsFile).getBoolean("hooktest", false)) loadApp(name = "com.luckyzyx.yuki", HookYukiDemo())
            if (prefs(PrefsFile).getBoolean("ad", false)) loadApp(name = "com.east2d.everyimage", HookMoreAnime.HookAd())
            if (prefs(PrefsFile).getBoolean("vip", false)) loadApp(name = "com.east2d.everyimage", HookMoreAnime.HookVip())
            if (prefs(PrefsFile).getBoolean("developer_mode",false)) loadApp(name = "com.android.systemui", HookSystemUI.HookDeveloper())
            if (prefs(PrefsFile).getBoolean("network_speed",false)) loadApp(name = "com.android.systemui", HookSystemUI.HookNetWorkSpeed())
        }
    }
}