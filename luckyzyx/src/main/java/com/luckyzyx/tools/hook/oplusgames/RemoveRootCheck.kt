package com.luckyzyx.tools.hook.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass

class RemoveRootCheck : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        val Member: Array<String> = when(prefs(PrefsFile).getString("OplusGamesCommit","null")){
            "f86f767","ce65873" -> arrayOf("com.oplus.x.c","b")

            else -> arrayOf("com.oplus.x.c","b")
        }
        //Remove Root Check
        //search -> dynamic_feature_cool_ex --> Method
        //("isSafe")) : null;
        findClass(Member[0]).hook {
            injectMember {
                method {
                    name = Member[1]
                    returnType = BundleClass
                }
                afterHook {
                    (result as Bundle).putInt("isSafe", 0)
                }
            }
        }
    }
}