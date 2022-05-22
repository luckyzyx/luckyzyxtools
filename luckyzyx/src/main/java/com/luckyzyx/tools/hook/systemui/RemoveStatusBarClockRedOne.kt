package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveStatusBarClockRedOne : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "setShouldShowOpStyle"
                    param(BooleanType)
                }
                beforeHook {
                    args(0).setFalse()
                }
            }
        }
    }
}