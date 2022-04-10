package com.luckyzyx.tools.hook

import android.content.pm.PackageManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.UnitType

class HookPackageInstaller {

    class ReplaceHook : YukiBaseHooker() {

        override fun onHook() {
            val packageInstallCommit = appContext.packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            ).metaData.getString("versionCommit")
            val methodName = arrayOfNulls<String>(6)
            val fieldName = arrayOfNulls<String>(2)
            if(packageInstallCommit == "a222497"){
                    methodName[0] = "M"
                    methodName[1] = "E"
                    methodName[2] = "j"
                    methodName[3] = "S"
                    methodName[4] = "T"
                    methodName[5] = "Q"
                    fieldName[0] = "aN"
                    fieldName[1] = "d"
            }
            findClass(name = "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
                //跳过检查安全应用跳转商店
                //isStartAppDetail
                //search -> count_canceled_by_app_detail -4
                if (!methodName.equals(null)) {
                    injectMember {
                        method {
                            name = methodName[0].toString()
                            returnType = BooleanType
                        }
                        afterHook {
                            resultFalse()
                        }
                    }
                }

                //跳过apk病毒扫描
                //checkToScanRisk
                //search -> "button_type", "install_old_version_button" -5
                //替换调用方法
                //initiateInstall
                //search -> "button_type", "install_old_version_button" -11
                if (!methodName[1].equals(null)) {
                    injectMember {
                        method {
                            name = methodName[1].toString()
                        }
                        replaceUnit {
                            method {
                                name = methodName[2].toString()
                            }.get(instance).call()
                        }
                    }
                }

                //方法 checkAppSuggest
                //search -> "PackageInstaller", "startAppdetail: " -7
                if (!methodName[3].equals(null)) {
                    injectMember {
                        method {
                            name = methodName[3].toString()
                            returnType = UnitType
                        }
                        replaceUnit {
                            resultNull()
                        }
                    }
                }
                //方法 checkGameSuggest
                //search -> "PackageInstaller", "don't recommend : -2
                if (!methodName[4].equals(null)) {
                    injectMember {
                        method {
                            name = methodName[4].toString()
                            returnType = UnitType
                        }
                        replaceUnit {
                            resultNull()
                        }
                    }
                }
                //mIsOPPOMarketExists
                //search -> "oppo_market"
                if (!fieldName[0].equals(null)) {
                    injectMember {
                        method {
                            name = "onCreate"
                            param(BundleClass)
                        }
                        afterHook {
                            field {
                                name = fieldName[0].toString()
                                type = BooleanType
                            }.get(instance).set(false)
                        }
                    }
                }
                //低版本相同版本警告
                //search ->  ? 1 : 0;
                if (!methodName[5].equals(null)) {
                    injectMember {
                        method {
                            name = methodName[5].toString()
                            returnType = BooleanType
                        }
                        replaceToFalse()
                    }
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
            //private LinearLayout mSuggestLayoutA;
            //***************
            //private RelativeLayout mSuggestLayoutATitle;
            //private LinearLayout mSuggestLayoutB;
            //private LinearLayout mSuggestLayoutC;

            //WYZ LinearLayout
            //X RelativeLayout
//            findClass(name = "com.android.packageinstaller.oplus.b").hook {
//                injectMember {
//                    method {
//                        name = "handleMessage"
//                    }
//                    afterHook {
//                        "com.android.packageinstaller.oplus.InstallAppProgress".clazz.hook {
//                            method {
//                                name = "a"
//                            }
//                            afterHook {
//                                "com.android.packageinstaller.oplus.InstallAppProgress".clazz.field{
//                                    name = "W"
//                                }.get(instance).setNull()
//                                "com.android.packageinstaller.oplus.InstallAppProgress".clazz.field{
//                                    name = "Y"
//                                }.get(instance).setNull()
//                                "com.android.packageinstaller.oplus.InstallAppProgress".clazz.field{
//                                    name = "Z"
//                                }.get(instance).setNull()
//                                "com.android.packageinstaller.oplus.InstallAppProgress".clazz.field{
//                                    name = "X"
//                                }.get(instance).setNull()
//                            }
//                        }
//                    }
//                }
//            }

            //hooklog
            //search -> OppoLog, isQELogOn =
            if(!fieldName[1].equals(null)){
                "com.android.packageinstaller.oplus.common.k".clazz.field {
                    name = fieldName[1].toString()
                }.get().set(true)
            }
        }
    }

    //ColorOS安装器替换为原生安装器
    //search -> DeleteStagedFileOnResult
    class ReplaceInstaller : YukiBaseHooker() {
        override fun onHook() {
            val packageInstallCommit = appContext.packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            ).metaData.getString("versionCommit")
            var methodName = ""
            var fieldName = ""
            if(packageInstallCommit == "a222497"){
                methodName = "com.android.packageinstaller.oplus.common.j"
                fieldName = "f"
            }else if (packageInstallCommit == "104bacb"){
                methodName = "com.android.packageinstaller.oplus.common.FeatureOption"
                fieldName = "sIsClosedSuperFirewall"
            }
            findClass(name = "com.android.packageinstaller.DeleteStagedFileOnResult").hook {
                if(!fieldName.equals(null)){
                    injectMember {
                        method {
                            name = "onCreate"
                            param(BundleClass)
                        }
                        beforeHook {
                            methodName.clazz.field {
                                name = fieldName
                            }.get().set(true)
                        }
                    }
                }
            }
        }
    }
}