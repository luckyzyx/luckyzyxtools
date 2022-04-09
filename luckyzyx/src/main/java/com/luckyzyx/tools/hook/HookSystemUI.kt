package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.LongType

class HookSystemUI {

    class HookNetWorkSpeed : YukiBaseHooker(){
        override fun onHook() {
            findClass(name = "com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
                injectMember {
                    method {
                        name = "updateNetworkSpeed"
                    }
                    beforeHook {
                        resultFalse()
                    }
                }
            }
        }
    }

    //屏蔽状态栏开发者选项警告
    class HookDeveloper : YukiBaseHooker(){
        private val DeveloperList = VariousClass(
            "com.oplusos.systemui.statusbar.policy.SystemPromptController",
            "com.coloros.systemui.statusbar.policy.SystemPromptController"
        )
        override fun onHook() {
            DeveloperList.hook {
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
}
