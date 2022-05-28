package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveStatusBarClockRedOne : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.ext.BaseClockExt").hook {
            injectMember {
                method {
                    name = "setTextWithRedOneStyle"
                    paramCount = 2
                }
                beforeHook {
                    field {
                        name = "mIsDateTimePanel"
                        type = BooleanType
                    }.get(instance).setFalse()
                }
            }
        }
    }
}