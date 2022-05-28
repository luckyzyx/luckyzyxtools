package com.luckyzyx.yukidebug.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.yukidebug.hook.systemui.Removestatusbarclockredone

class HookSystenUI : YukiBaseHooker() {
    override fun onHook() {
        loadHooker(Removestatusbarclockredone())
    }
}