package com.luckyzyx.tools.hook.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.cloud.base.commonsdk.baseutils.al").hook {
            injectMember {
                method {
                    name = "a"
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(2)
            }
        }
    }
}