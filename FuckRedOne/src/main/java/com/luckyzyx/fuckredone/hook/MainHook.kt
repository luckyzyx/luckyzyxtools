package com.luckyzyx.fuckredone.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {
    override fun onInit() {
        configs {
            debugTag = "FuckRedOne"
            isDebug = false
        }
    }

    override fun onHook() = encase {
        //SystemUI
        loadApp("com.android.systemui"){
            //StatusBarClick->C12
            findClass("com.android.systemui.statusbar.policy.Clock").hook {
                injectMember {
                    method {
                        name = "setShouldShowOpStyle"
                        param(BooleanType)
                    }
                    beforeHook {
                        args(0).setFalse()
                    }
                }.onNoSuchMemberFailure {
                    loggerE(msg = "MethodNotFound->setTextWithRedOneStyle")
                }
            }
            //StatusBarClick->C12.1
            findClass("com.oplusos.systemui.ext.BaseClockExt").hook {
                injectMember {
                    method {
                        name {
                            //C12.1
                            equalsOf(other = "setTextWithRedOneStyle",isIgnoreCase = false)
                        }
                        paramCount = 2
                    }
                    beforeHook {
                        field {
                            name = "mIsDateTimePanel"
                            type = BooleanType
                        }.get(instance).setFalse()
                    }
                }.onNoSuchMemberFailure {
                    loggerE(msg = "MethodNotFound->setTextWithRedOneStyle")
                }
            }.onHookClassNotFoundFailure {
                loggerE(msg = "ClassNotFound->BaseClockExt")
            }
            //LockScreen
            "com.oplusos.systemui.keyguard.clock.RedTextClock".clazz.field {
                name = "NUMBER_ONE"
            }.get().set("")
        }
        //AlarmClock
        loadApp("com.coloros.alarmclock"){
            //ClockWidget
            "com.coloros.widget.smallweather.OnePlusWidget".clazz.field {
                name {
                    equalsOf(other = "Sb",isIgnoreCase = false)//9RT->C12
                    equalsOf(other = "Sc",isIgnoreCase = false)//9RT->C12.1
                }
                type(CharSequenceType)
            }.get().set("")
        }
    }
}