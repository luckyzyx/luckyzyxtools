package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType

class HookSystemUI {

    class HookNetworkSpeed : YukiBaseHooker() {
        override fun onHook() {

            findClass(name = "com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
                // 拦截方法传入形参
                injectMember {
                    method {
                        name = "postUpdateNetworkSpeedDelay"
                        param(LongType)
                        returnType = LongType
                    }
                    beforeHook {
                        // 设置 0 号 param
                        if (args().equals(4000)) args().set(1000)
                    }
                }
            }

        }

    }


}
