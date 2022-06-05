package com.luckyzyx.tools.hook.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class AllowReplaceInstall : YukiBaseHooker() {
    override fun onHook() {
        //Allow replace install,Low/same version warning
        //search ->  ? 1 : 0; -> this Method
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method {
                    name {
                        equalsOf(other = "Q",isIgnoreCase = false)//7bc7db7,e1a2c58,a222497
                        equalsOf(other = "P",isIgnoreCase = false)//75fe984,532ffef
                        equalsOf(other = "R",isIgnoreCase = false)//38477f0
                        equalsOf(other = "isReplaceInstall",isIgnoreCase = false)//d132ce2,faec6ba
                    }
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}