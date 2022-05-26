package com.luckyzyx.clockredone.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {
    override fun onInit() {
        configs {
            debugTag = "StatusBarClockRedOne"
            isDebug = false
        }
    }

    override fun onHook() = encase(){
        loadApp("com.android.systemui"){
            findClass("com.oplusos.systemui.ext.BaseClockExt").hook {
                injectMember {
                    method {
                        name = "setTextWithRedOneStyle"
                        paramCount = 2
                    }
                    beforeHook {
                        field {
                            name = "mIsDateTimePanel"
                            type = BooleanType
                        }.get(instance).setFalse()
                    }
                }
            }
        }
    }
}