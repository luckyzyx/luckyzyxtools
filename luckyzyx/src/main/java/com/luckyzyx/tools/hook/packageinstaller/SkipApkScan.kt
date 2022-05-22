package com.luckyzyx.tools.hook.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class SkipApkScan : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        val list1: Array<String> =
            when (prefs(PrefsFile).getString("PackageInstallCommit", "null")) {
                "7bc7db7", "e1a2c58" -> {
                    arrayOf("L", "C", "K")
                }
                "75fe984", "532ffef" -> {
                    arrayOf("L", "D", "i")
                }
                "38477f0" -> {
                    arrayOf("M", "D", "k")
                }
                "a222497" -> {
                    arrayOf("M", "E", "j")
                }
                //d132ce2
                else -> {
                    arrayOf("isStartAppDetail", "checkToScanRisk", "initiateInstall")
                }
            }

        //skip appdetail,search isStartAppDetail
        //search -> count_canceled_by_app_detail -4 -> MethodName
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method {
                    name = list1[0]
                }
                afterHook {
                    resultFalse()
                }
            }
        }

        //skip app scan, search method checkToScanRisk
        //search -> "button_type", "install_old_version_button" -5 -> MethodName
        //replace to initiateInstall
        //search -> "button_type", "install_old_version_button" -11 -> MethodName
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method {
                    name = list1[1]
                }
                replaceUnit {
                    method {
                        name = list1[2]
                    }.get(instance).call()
                }
            }
        }
    }
}