package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.tools.utils.XSPUtils

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
                    if (XSPUtils.getBoolean("network_speed",false)) args().set(0x3e8)
                }
            }
        }
    }
}
