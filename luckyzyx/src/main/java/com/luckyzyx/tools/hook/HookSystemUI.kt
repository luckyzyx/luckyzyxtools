package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType

class HookSystemUI : YukiBaseHooker() {

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
