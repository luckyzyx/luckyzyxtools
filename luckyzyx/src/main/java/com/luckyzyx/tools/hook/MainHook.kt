package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.YukiHookXposedInitProxy
import com.luckyzyx.tools.utils.XSPUtils

@InjectYukiHookWithXposed
class MainHook : YukiHookXposedInitProxy {

    override fun onHook() {
        YukiHookAPI.configs {
            // 全局调试用的 TAG
            debugTag = "YukiHook"
            // 是否开启调试模式
            isDebug = true
            // 默认为开启状态，开启后模块将会向 Logcat 和 XposedBridge.log 打印详细的 Hook 日志，关闭后仅会打印 E 级别的日志
            isAllowPrintingLogs = false
        }
        YukiHookAPI.encase {
            if (XSPUtils.getBoolean("hooktest",false)) {
                loadApp(name = "com.luckyzyx.yuki", HookYukiDemo())
            }

            if (XSPUtils.getBoolean("ad",false)) {
                loadApp(name = "com.east2d.everyimage", HookMoreAnime.HookAdOpen())
            }
            if (XSPUtils.getBoolean("vip",false)) {
                loadApp(name = "com.east2d.everyimage", HookMoreAnime.HookIsVip())
            }
            if (XSPUtils.getBoolean("network_speed",false)) {
                loadApp(name = "com.android.systemui", HookSystemUI.HookNetworkSpeed())
            }
        }
    }
}