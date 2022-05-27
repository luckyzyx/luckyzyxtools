package com.luckyzyx.tools.hook.alarmclock

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveAlarmClockWidgetRedOne : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        val list: Array<String> = when (prefs(PrefsFile).getString("AlarmClockCommit", "null")) {
                "7ce00ef" -> arrayOf("Sb","z")
                "c3d4fc6" -> arrayOf("Sc","z")
                //c3d4fc6->9RT12.1
                else -> arrayOf("Sc","z")
            }
        findClass("com.coloros.widget.smallweather.OnePlusWidget").hook {
            injectMember {
                method {
                    name = list[1]
                }
                beforeHook {
                    field {
                        name = list[0]
                        type = CharSequenceType
                    }.get().set("")
                }
            }
        }
//        "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
//            name = list[0]
//            type = CharSequenceType
//        }.get().set("")
    }
}