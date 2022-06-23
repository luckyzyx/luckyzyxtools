package com.luckyzyx.tools.hook.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE

class RemoveStatusBarTopNotification : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.android.server.wm.AlertWindowNotification").hook {
            injectMember {
                method {
                    name = "onPostNotification"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("remove_statusbar_top_notification", false)) resultNull()
                }
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->AlertWindowNotification")
        }
    }
}