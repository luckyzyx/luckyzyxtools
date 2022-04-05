package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class HookSystemUI {

    class HookNetWorkSpeed : YukiBaseHooker(){
        override fun onHook() {
            findClass(name = "com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
                // 拦截方法传入形参
                injectMember {
                    method {
                        name = "postUpdateNetworkSpeedDelay"
                    }
                    beforeHook {
                        args().set(1000L)
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
