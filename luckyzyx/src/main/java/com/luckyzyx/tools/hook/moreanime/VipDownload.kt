package com.luckyzyx.tools.hook.moreanime

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class VipDownload : YukiBaseHooker() {
    override fun onHook() {
        findClass(name = "com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity").hook {
            injectMember {
                method {
                    name = "isVip"
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}