package com.luckyzyx.tools.hook.android

import android.view.WindowManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class DisableFlagSecure : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("android.view.Window").hook {
            injectMember {
                method {
                    name = "setFlags"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)){
                        var flags: Int = args[0] as Int
                        flags = flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                        args(0).set(flags)
                    }
                }
            }
        }
        findClass("android.view.SurfaceView").hook {
            injectMember {
                method {
                    name = "setSecure"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)) args(0).setFalse()
                }
            }
        }
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
        findClass("android.view.WindowManagerGlobal").hook {
            injectMember {
                method {
                    name = "addView"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)){
                        val params = args[1] as WindowManager.LayoutParams
                        params.flags = params.flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                    }
                }
            }
            injectMember {
                method {
                    name = "updateViewLayout"
                }
                beforeHook {
                    if (prefs(PrefsFile).getBoolean("disable_flag_secure", false)) {
                        val params = args[1] as WindowManager.LayoutParams
                        params.flags = params.flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                    }
                }
            }
        }
    }
}