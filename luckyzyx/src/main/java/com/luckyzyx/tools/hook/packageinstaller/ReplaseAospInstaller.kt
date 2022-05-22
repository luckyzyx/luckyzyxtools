package com.luckyzyx.tools.hook.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass

class ReplaseAospInstaller : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        val list3: Array<String> = when (prefs(PrefsFile).getString("PackageInstallCommit","null")) {
            "7bc7db7", "e1a2c58", "75fe984", "532ffef", "38477f0", "a222497" -> {
                arrayOf(
                    "com.android.packageinstaller.DeleteStagedFileOnResult",
                    "com.android.packageinstaller.oplus.common.j",
                    "f"
                )
            }
            //d132ce2
            else -> {
                arrayOf(
                    "com.android.packageinstaller.DeleteStagedFileOnResult",
                    "com.android.packageinstaller.oplus.common.FeatureOption",
                    "sIsClosedSuperFirewall"
                )
            }
        }
        //use AOSP installer,search -> DeleteStagedFileOnResult
        findClass(list3[0]).hook{
            injectMember {
                method {
                    name = "onCreate"
                    param(BundleClass)
                }
                beforeHook {
                    list3[1].clazz.field {
                        name = list3[2]
                    }.get().setTrue()
                }
            }
        }

    }
}