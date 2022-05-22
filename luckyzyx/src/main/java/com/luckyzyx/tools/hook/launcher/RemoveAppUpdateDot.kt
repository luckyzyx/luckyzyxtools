package com.luckyzyx.tools.hook.launcher

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveAppUpdateDot : YukiBaseHooker() {
    override fun onHook() {
        findClass(name =  "com.android.launcher3.OplusBubbleTextView").hook {
            injectMember {
                method {
                    name = "applyLabel"
                    paramCount = 3
                }
                beforeHook {
                    val field = "com.android.launcher3.model.data.ItemInfo".clazz.getDeclaredField("title")
                    field.isAccessible = true
                    val title = field[args[0]] as CharSequence
                    (instance as TextView).text = title
                    resultNull()
                }
            }
        }
    }
}