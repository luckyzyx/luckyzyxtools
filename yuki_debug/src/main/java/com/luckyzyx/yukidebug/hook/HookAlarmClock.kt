package com.luckyzyx.yukidebug.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.yukidebug.hook.alarmclock.Removeclockwidgetredone

class HookAlarmClock : YukiBaseHooker() {
    override fun onHook() {
        loadHooker(Removeclockwidgetredone())
    }
}