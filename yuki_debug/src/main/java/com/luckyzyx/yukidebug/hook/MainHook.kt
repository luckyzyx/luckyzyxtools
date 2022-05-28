package com.luckyzyx.yukidebug.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {
    override fun onInit() {
        configs {
            debugTag = "YukiDebug"
            isDebug = true
        }
    }

    override fun onHook() = encase {
        loadApp("com.android.systemui",HookSystenUI())
        loadApp("com.coloros.alarmclock",HookAlarmClock())
    }
}