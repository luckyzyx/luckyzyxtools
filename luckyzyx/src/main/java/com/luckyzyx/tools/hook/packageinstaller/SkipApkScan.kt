package com.luckyzyx.tools.hook.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class SkipApkScan : YukiBaseHooker() {
    override fun onHook() {

        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            //skip appdetail,search isStartAppDetail
            //search -> count_canceled_by_app_detail -4 -> MethodName
            injectMember {
                method {
                    name {
                        equalsOf(other = "L",isIgnoreCase = false)//7bc7db7,e1a2c58,75fe984,532ffef
                        equalsOf(other = "M",isIgnoreCase = false)//38477f0,a222497
                        equalsOf(other = "isStartAppDetail",isIgnoreCase = false)//d132ce2,faec6ba
                    }
                }
                afterHook {
                    resultFalse()
                }
            }
            //skip app scan, search method checkToScanRisk
            //search -> "button_type", "install_old_version_button" -5 -> MethodName
            //replace to initiateInstall
            //search -> "button_type", "install_old_version_button" -11 -> MethodName
            injectMember {
                method {
                    name {
                        equalsOf(other = "C",isIgnoreCase = false)//7bc7db7,e1a2c58
                        equalsOf(other = "D",isIgnoreCase = false)//75fe984,532ffef,38477f0
                        equalsOf(other = "E",isIgnoreCase = false)//a222497
                        equalsOf(other = "checkToScanRisk",isIgnoreCase = false)//d132ce2,faec6ba
                    }
                }
                replaceUnit {
                    method {
                        name {
                            equalsOf(other = "K",isIgnoreCase = false)//7bc7db7,e1a2c58
                            equalsOf(other = "i",isIgnoreCase = false)//75fe984,532ffef
                            equalsOf(other = "k",isIgnoreCase = false)//38477f0
                            equalsOf(other = "j",isIgnoreCase = false)//a222497
                            equalsOf(other = "initiateInstall",isIgnoreCase = false)//d132ce2,faec6ba
                        }
                    }.get(instance).call()
                }
            }
        }
    }
}