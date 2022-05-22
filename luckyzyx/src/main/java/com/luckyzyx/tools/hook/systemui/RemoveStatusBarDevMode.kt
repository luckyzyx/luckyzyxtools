package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarDevMode : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.oplusos.systemui.statusbar.policy.SystemPromptController",
            "com.coloros.systemui.statusbar.policy.SystemPromptController"
        ).hook {
            injectMember {
                method {
                    name = "updateDeveloperMode"
                }
                beforeHook {
                    resultNull()
                }
            }
        }
    }
}