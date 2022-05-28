package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field

class RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
//        findClass("com.oplusos.systemui.keyguard.clock.RedTextClock").hook {
//            injectMember {
//                constructor {
//                    paramCount = 2
//                }
//                afterHook {
//                    field {
//                        name = "NUMBER_ONE"
//                    }.get().set("")
//                }
//            }
//        }
        "com.oplusos.systemui.keyguard.clock.RedTextClock".clazz.field {
            name = "NUMBER_ONE"
        }.get().set("")
    }
}