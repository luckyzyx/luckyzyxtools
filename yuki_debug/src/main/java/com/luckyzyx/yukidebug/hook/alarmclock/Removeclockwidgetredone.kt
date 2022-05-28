package com.luckyzyx.yukidebug.hook.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class Removeclockwidgetredone : YukiBaseHooker() {
    override fun onHook() {
        "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
            name = "Sc"
            type = CharSequenceType
        }.get().set("")
    }
}