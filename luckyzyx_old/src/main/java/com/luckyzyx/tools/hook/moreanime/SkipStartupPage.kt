package com.luckyzyx.tools.hook.moreanime

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class SkipStartupPage : YukiBaseHooker() {
    override fun onHook() {
        findClass(name = "com.east2d.haoduo.ui.activity.SplashActivity").hook {
            injectMember {
                method {
                    name = "isAdOpen"
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}