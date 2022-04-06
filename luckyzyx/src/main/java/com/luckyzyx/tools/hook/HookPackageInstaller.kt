package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType

class HookPackageInstaller {

    class ReplaceHook : YukiBaseHooker() {

        override fun onHook() {
            findClass(name = "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
                //跳过检查安全应用跳转商店
                //search -> count_canceled_by_app_detail -4
                injectMember {
                    method {
                        name = "M"
                        returnType = BooleanType
                    }
                    afterHook {
                        resultFalse()
                    }
                }

                //跳过apk病毒扫描
                //checkToScanRisk
                //search -> "button_type", "install_old_version_button" -5
                //替换调用方法
                //initiateInstall
                //search -> "button_type", "install_old_version_button" -11
                injectMember {
                    method {
                        name = "E"
                    }
                    replaceUnit {
                        method {
                            name = "j"
                        }.get(instance).call()
                    }
                }

                //方法 checkAppSuggest
                //search -> "PackageInstaller", "startAppdetail: " -7
                injectMember {
                    method {
                        name = "S"
                        returnType = UnitType
                    }
                    replaceUnit {
                        resultNull()
                    }
                }
                //方法 checkGameSuggest
                //search -> "PackageInstaller", "don't recommend : -2
                injectMember {
                    method {
                        name = "T"
                        returnType = UnitType
                    }
                    replaceUnit {
                        resultNull()
                    }
                }

                //mIsOPPOMarketExists
                //search -> "oppo_market"
                injectMember {
                    method {
                        name = "onCreate"
                        param(BundleClass)
                    }
                    afterHook {
                        field {
                            name = "aN"
                            type = BooleanType
                        }.get(instance).set(false)
                    }
                }

                //低版本相同版本警告
                //search ->  ? 1 : 0;
                injectMember {
                    method {
                        name = "Q"
                        returnType = BooleanType
                    }
                    replaceToFalse()
                }
            }

            //uncheck app_suggest_option as default 取消选中默认应用建议选项
            //search -> CompoundButton.SavedState{
            findClass(name = "com.coui.appcompat.widget.COUICheckBox").hook {
                injectMember {
                    method {
                        name = "setState"
                        param(IntType)
                    }
                    beforeHook {
                        if (args().equals(2)) {
                            args().set(0)
                        }
                    }
                }
            }

            //安装成功时隐藏建议布局
//            findClass(name = "com.android.packageinstaller.oplus.InstallAppProgress").hook {
//                injectMember {
//                    method {
//                        name = "a"
//                    }
//                    afterHook {
//                        findClass(name = "com.android.packageinstaller.oplus.InstallAppProgress").hook {
//                            field {
//                                name = "W"
//                            }.get().self
//                        }
//                    }
//                }
//            }
        }
    }

    //ColorOS安装器替换为原生安装器
    //search -> DeleteStagedFileOnResult
    class ReplaceInstaller : YukiBaseHooker() {
        override fun onHook() {
            findClass(name = "com.android.packageinstaller.DeleteStagedFileOnResult").hook {
                injectMember {
                    method {
                        name = "onCreate"
                        param(BundleClass)
                    }
                    beforeHook {
                        findClass(name = "com.android.packageinstaller.oplus.common.j").hook {
                            injectMember {
                                field {
                                    name = "f"
                                }.get().set(true)
                            }
                        }
                    }
                }
            }
        }
    }
}