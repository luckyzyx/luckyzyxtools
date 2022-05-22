package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveChargingCompleted : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings").hook {
            injectMember {
                method {
                    name = "showChargeErrorDialog"
                    param(IntType)
                }
                beforeHook {
                    if (args(0).equals(7)) args(0).setNull()
                }
            }
        }
    }
}