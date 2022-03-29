package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType

class HookSystemUI {

    class HookNetWorkSpeed : YukiBaseHooker(){
        override fun onHook() {
            findClass(name = "com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
                // 拦截方法传入形参
                injectMember {
                    method {
                        name = "postUpdateNetworkSpeedDelay"
                        param(LongType)
                    }
                    beforeHook {
                        args().set(0x3e8)
                    }
                }
            }
        }
    }
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
