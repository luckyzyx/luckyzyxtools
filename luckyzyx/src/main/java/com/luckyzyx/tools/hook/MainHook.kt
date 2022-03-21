package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.YukiHookAPI.encase
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.xposed.proxy.YukiHookXposedInitProxy
import com.luckyzyx.tools.utils.XSPUtils

@InjectYukiHookWithXposed
class MainHook : YukiHookXposedInitProxy {

    override fun onHook() = encase {
        configs {
            // 全局调试 TAG
            debugTag = "YukiHook"
            // 默认为开启状态，开启后模块将会向 Logcat 和 XposedBridge.log 打印详细的 Hook 日志，关闭后仅会打印 E 级别的日志
            isDebug = false
            // 是否启用调试日志的输出功能,关闭后将会停用 YukiHookAPI 对全部日志的输出.但是不影响当你手动调用日志方法输出日志
            isAllowPrintingLogs = true
        }
        if(XSPUtils.getBoolean("hooktest",false)) {
            loadApp(name = "com.luckyzyx.yuki", HookYukiDemo())
        }
        loadApp(name = "com.east2d.everyimage", HookMoreAnime())
//        loadApp(name = "com.android.systemui", HookSystemUI())
    }
}