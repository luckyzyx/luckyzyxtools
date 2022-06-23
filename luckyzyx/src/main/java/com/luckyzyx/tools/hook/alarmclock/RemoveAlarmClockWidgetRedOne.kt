package com.luckyzyx.tools.hook.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    override fun onHook() {
        "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
            name {
                equalsOf(other = "Sb",isIgnoreCase = false)//9RT->C12
                equalsOf(other = "Sc",isIgnoreCase = false)//9RT->C12.1 C02
                equalsOf(other = "TR",isIgnoreCase = false)//9RT->C12.1 C03
            }
            type = CharSequenceType
        }.get().set("")
    }
}