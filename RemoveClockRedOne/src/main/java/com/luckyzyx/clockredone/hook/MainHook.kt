package com.luckyzyx.clockredone.hook

import android.os.Build.VERSION.SDK_INT
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

    override fun onHook() = encase {
        loadApp("com.android.systemui"){
            if (SDK_INT == 30){
                findClass("com.android.systemui.statusbar.policy.Clock").hook {
                    injectMember {
                        method {
                            name = "setShouldShowOpStyle"
                            param(BooleanType)
                        }
                        beforeHook {
                            args(0).setFalse()
                        }
                    }
                }
            }else{
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
}