package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field

class HookAlarmClock : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //移除桌面时钟组件红一
        val list: String =
            when (prefs(PrefsFile).getString("AlarmClockCommit", "null")) {
                "7ce00ef" -> "Sb"
                "c3d4fc6" -> "Sc"
                else -> "Sb"
            }
        if (prefs(PrefsFile).getBoolean("remove_alarmclick_widget_redone",false)) {
            appClassLoader.loadClass("com.coloros.widget.smallweather.OnePlusWidget").field {
                name = list
            }.get().set("")
        }
    }
}