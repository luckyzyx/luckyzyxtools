package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarSecurePayment : YukiBaseHooker() {
    override fun onHook() {
        //图标
        findClass("com.oplusos.systemui.ext.SecurePaymentControllerExt").hook {
            injectMember {
                method {
                    name = "handlePaymentDetectionMessage"
                    paramCount = 1
                }
                beforeHook {
                    resultNull()
                }
            }
        }
    }
}