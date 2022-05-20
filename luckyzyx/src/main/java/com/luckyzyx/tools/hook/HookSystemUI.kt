package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.LongType

class HookSystemUI : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //设置状态栏网速刷新率
        if (prefs(PrefsFile).getBoolean("set_network_speed",false)){
            findClass(name = "com.oplusos.systemui.statusbar.controller.NetworkSpeedController").hook {
                injectMember {
                    method {
                        name = "postUpdateNetworkSpeedDelay"
                        param(LongType)
                    }
                    beforeHook {
                        args(0).set(1000L)
                    }
                }
            }
        }

        //移除充电完成通知
        if (prefs(PrefsFile).getBoolean("remove_charging_completed",false)){
            findClass(name =  "com.oplusos.systemui.notification.power.OplusPowerNotificationWarnings").hook {
                injectMember {
                    method {
                        name = "showChargeErrorDialog"
                        param(IntType)
                    }
                    beforeHook {
                        if (args(0).equals(7)) args(0).setNull()
                    }
                }
            }
        }

        //移除状态栏时钟红1
        if (prefs(PrefsFile).getBoolean("remove_statusbar_clock_redone",false)){
            findClass(name = "com.android.systemui.statusbar.policy.Clock").hook {
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
        }

        //移除锁屏时钟红1
        if (prefs(PrefsFile).getBoolean("remove_lock_screen_redone",false)){
          appClassLoader.loadClass("com.oplusos.systemui.keyguard.clock.RedTextClock").field {
                name = "NUMBER_ONE"
            }.get().set("")
        }

        //设置下拉状态栏时钟显秒
        if (prefs(PrefsFile).getBoolean("statusbar_clock_show_second",false)){
            findClass(name = "com.android.systemui.statusbar.policy.Clock").hook {
                injectMember {
                    method {
                        name = "setShowSecondsAndUpdate"
                        param(BooleanType)
                    }
                    beforeHook {
                        args(0).setTrue()
                    }
                }
            }
        }

        //移除状态栏开发者选项警告
        if (prefs(PrefsFile).getBoolean("remove_statusbar_devmode",false)){
            VariousClass(
                "com.oplusos.systemui.statusbar.policy.SystemPromptController",
                "com.coloros.systemui.statusbar.policy.SystemPromptController"
            ).hook {
                injectMember {
                    method {
                        name = "updateDeveloperMode"
                    }
                    beforeHook {
                        resultNull()
                    }
                }
            }
        }

        //移除下拉状态栏底部网络警告
        if (prefs(PrefsFile).getBoolean("remove_statusbar_bottom_networkwarn",false)){
            findClass(name =  "com.oplusos.systemui.qs.widget.OplusQSSecurityText").hook {
                injectMember {
                    method {
                        name = "handleRefreshState"
                    }
                    beforeHook {
                        resultNull()
                    }
                }
            }
        }
    }
}
