package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field

class RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
        "com.oplusos.systemui.keyguard.clock.RedTextClock".clazz.field {
            name = "NUMBER_ONE"
        }.get().set("")
    }
}