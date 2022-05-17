package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookAndroid : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.android.server.wm.WindowState").hook {
            injectMember {
                method {
                    name = "isSecureLocked"
                    returnType = BooleanType
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)) resultFalse()
                }
            }
        }

        findClass("com.android.server.wm.AlertWindowNotification").hook {
            injectMember {
                method {
                    name = "onPostNotification"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("remove_statusbar_top_notification", false)) resultNull()
                }
            }
        }
    }
}
