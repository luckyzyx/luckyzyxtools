package com.luckyzyx.tools.hook.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        val list: String =
            when (prefs(PrefsFile).getString("AlarmClockCommit", "null")) {
                "7ce00ef" -> "Sb"
                "c3d4fc6" -> "Sc"
                //c3d4fc6->9RT12.1
                else -> "Sc"
            }
        "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
            name = list
            type = CharSequenceType
        }.get().set("")
    }
}