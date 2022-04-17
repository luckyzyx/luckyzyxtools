package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class HookAndroid {

    //移除状态栏上层通知警告
    class HookTopNotification : YukiBaseHooker(){
        override fun onHook() {
            findClass(name = "com.android.server.wm.AlertWindowNotification").hook {
                injectMember {
                    method {
                        name = "onPostNotification"
                    }
                    beforeHook {
                        resultNull()
                    }
                }
            }
        }
    }

    class DisableFlagSecure : YukiBaseHooker() {

        override fun onHook() {
            findClass(name = "com.android.server.wm.WindowState").hook {
                injectMember {
                    method {
                        name = "isSecureLocked"
                    }
                    beforeHook {
                        resultFalse()
                    }
                }
            }
        }
    }
}
