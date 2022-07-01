package com.luckyzyx.tools.hook.safecenter

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

class UnlockStartupLimit : YukiBaseHooker() {
    override fun onHook() {
        //Unlock Startup Limit --> Source StratupManager
        //search -> "update max allow count ? " -5 --> method,-1 --> field
        VariousClass(
            "com.oplus.safecenter.startupapp.a", //6f0072e
        ).clazz.hook {
            injectMember {
                method {
                    name {
                        equalsOf(other = "b",isIgnoreCase = false)
                    }
                    param(ContextClass)
                    returnType = null
                }
                afterHook {
                    field {
                        name {
                            equalsOf(other = "d",isIgnoreCase = false)
                        }
                        type = IntType
                    }.get().set(10000)
                }
            }
        }
    }
}