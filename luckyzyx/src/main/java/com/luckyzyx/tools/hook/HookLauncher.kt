package com.luckyzyx.tools.hook

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import java.lang.reflect.Field

class HookLauncher {

    //解锁最近任务限制
    class UnlockTaskLocks : YukiBaseHooker(){
        override fun onHook() {
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
    }

    //移除APP更新蓝点
    class RemoveAppUpdateDot : YukiBaseHooker(){
        override fun onHook() {
            findClass(name =  "com.android.launcher3.OplusBubbleTextView").hook {
                injectMember {
                    method {
                        name = "applyLabel"
                        paramCount = 3
                    }
                    beforeHook {
                        val field:Field = "com.android.launcher3.model.data.ItemInfo".clazz.getDeclaredField("title")
                        field.isAccessible = true
                        val title = field[args(0)] as CharSequence
                        (method as TextView).text = title
                        resultNull()
                    }
                }
            }
        }
    }
}
