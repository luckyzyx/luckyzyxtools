package com.luckyzyx.yukidebug.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field

class Removestatusbarclockredone : YukiBaseHooker() {
    override fun onHook() {
        "com.oplusos.systemui.keyguard.clock.RedTextClock".clazz.field {
            name = "NUMBER_ONE"
        }.get().set("")
    }
}