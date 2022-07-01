package com.luckyzyx.tools.hook.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass

class RemoveRootCheck : YukiBaseHooker() {
    override fun onHook() {
        //Remove Root Check --> Source COSASDKManager
        //search -> dynamic_feature_cool_ex --> Method
        //("isSafe")) : null; --> isSafe:0
        VariousClass(
            "com.oplus.x.c", //f86f767,ce65873
            "com.oplus.f.c", //424d87a
        ).hook {
            injectMember {
                method {
                    name {
                        equalsOf(other = "b",isIgnoreCase = false)
                    }
                    returnType = BundleClass
                }
                afterHook {
                    (result as Bundle).putInt("isSafe", 0)
                }
            }
        }
    }
}