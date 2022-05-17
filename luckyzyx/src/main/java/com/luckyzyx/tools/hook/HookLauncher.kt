package com.luckyzyx.tools.hook

import android.annotation.SuppressLint
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class HookLauncher : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    @SuppressLint("PrivateApi")
    override fun onHook() {
        //解锁最近任务限制
        if (prefs(PrefsFile).getBoolean("unlock_task_locks",false)){
            findClass(name = "com.oplus.quickstep.applock.OplusLockManager").hook {
                injectMember {
                    method {
                        name = "OplusLockManager"
                        paramCount = 1
                    }
                    afterHook {
                        field {
                            name = "mLockAppLimit"
                        }.get(instance).set(999)
                    }
                }
            }
        }

        //移除APP更新蓝点
        if (prefs(PrefsFile).getBoolean("remove_app_update_dot",false)){
            findClass(name =  "com.android.launcher3.OplusBubbleTextView").hook {
                injectMember {
                    method {
                        name = "applyLabel"
                        paramCount = 3
                    }
                    beforeHook {
                        val field = appClassLoader.loadClass("com.android.launcher3.model.data.ItemInfo").getDeclaredField("title")
                        field.isAccessible = true
                        val title = field[args[0]] as CharSequence
                        (instance as TextView).text = title
                        resultNull()
                    }
                }
            }
        }
    }
}
