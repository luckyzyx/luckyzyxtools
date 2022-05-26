package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveChargingCompleted : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings",
            "com.coloros.systemui.notification.power.ColorosPowerNotificationWarnings"
        ).hook {
            injectMember {
                method {
                    name = "showChargeErrorDialog"
                    param(IntType)
                }
                beforeHook {
                    if (args().first().int() == 7) resultNull()
                }
            }
        }
    }
}