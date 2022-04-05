package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class HookPackageInstaller : YukiBaseHooker() {
    override fun onHook() {
        //移除验证
        findClass(name = "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            //跳过安装指南
            //search -> count_canceled_by_app_detail -3
            injectMember {
                method {
                    name = "M"
                }
                afterHook {
                    resultFalse()
                }
            }
            //帐户验证
//            injectMember {
//                method {
//                    name = ""
//                }
//            }

            //apk扫描
//            injectMember {
//                method {
//                    name = "E"
//                }
//                replaceUnit {
//                    method {
//                        name = "j"
//                    }
//                    resultNull()
//                }
//            }

            //版本警告
            //search ->  ? 1 : 0;
            injectMember {
                method {
                    name = "P"
                }
                replaceToFalse()
            }

            //      uncheck app_suggest_option as default
            //      search -> CompoundButton.SavedState{
            findClass(name = "com.coui.appcompat.widget.COUICheckBox").hook {
                injectMember {
                    method {
                        name = "setState"
                        param(IntType)
                    }
                    beforeHook {
                        if(args().equals(2)){
                            args().set(0)
                        }
                    }
                }
            }
        }
    }
}