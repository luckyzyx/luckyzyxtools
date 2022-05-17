package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookPackageInstaller {

    class SkipScan : YukiBaseHooker() {
        override fun onHook() {
            val list: Array<String> = when(prefs("XposedSettings").getString("PackageInstallCommit","null")){
            "7bc7db7","e1a2c58" -> { arrayOf("L","C","K") }
            "75fe984","532ffef" -> { arrayOf("L","D","i") }
            "38477f0" -> { arrayOf("M","D","k") }
            "a222497" -> { arrayOf("M","E","j") }
            //d132ce2
            else -> { arrayOf("isStartAppDetail","checkToScanRisk","initiateInstall") }
            }

            //skip appdetail,search isStartAppDetail
            //search -> count_canceled_by_app_detail -4 -> MethodName
            findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
                injectMember {
                    method {
                        name = list[0]
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
                        name = list[1]
                    }
                    replaceUnit {
                        method {
                            name = list[2]
                        }.get(instance).call()
                    }
                }
            }
        }
    }

    class AllowReplace : YukiBaseHooker() {
        override fun onHook() {
            val list: String = when(prefs("XposedSettings").getString("PackageInstallCommit","null")){
                "7bc7db7","e1a2c58","a222497" -> "Q"
                "75fe984","532ffef" -> "P"
                "38477f0" -> "R"
                //d132ce2
                else -> "isReplaceInstall"
            }
            //Allow replace install,Low/same version warning
            //search ->  ? 1 : 0; -> this Method
            findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
                injectMember {
                    method {
                        name = list
                        returnType = BooleanType
                    }
                    replaceToFalse()
                }
            }
        }
    }

    //ColorOS安装器替换为原生安装器
    //search -> DeleteStagedFileOnResult
    class ReplaceInstaller : YukiBaseHooker() {
        override fun onHook() {
            val list: Array<String> = when (prefs("XposedSettings").getString("PackageInstallCommit","null")) {
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
            findClass(list[0]).hook{
                injectMember {
                    method {
                        name = "onCreate"
                        param(BundleClass)
                    }
                    beforeHook {
                        list[1].clazz.field {
                            name = list[2]
                        }.get().setTrue()
                    }
                }
            }
        }
    }
}