package com.luckyzyx.tools.hook

import android.app.AlertDialog
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerI
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.highcapable.yukihookapi.hook.type.java.UnitType

class HookYukiDemo : YukiBaseHooker() {

    override fun onHook() {
        findClass(name = "$packageName.ui.MainActivity").hook {
            // 替换返回值
            injectMember {
                method {
                    name = "getFirstText"
                    returnType = StringType
                }
                replaceTo(any = "Hello YukiHookAPI!")
            }
            // 拦截类定义参数
            injectMember {
                method {
                    name = "onCreate"
                    param(BundleClass)
                }
                beforeHook {
                    field {
                        name = "secondText"
                        type = StringType
                    }.get(instance).set("I am hook result")
                }
            }
            // 拦截方法传入形参
            injectMember {
                method {
                    name = "getRegularText"
                    param(StringType)
                    returnType = StringType
                }
                beforeHook {
                    // 设置 0 号 param
                    args().set("I am hook method param")
                }
            }
            // 拦截替换整个方法
            injectMember {
                method {
                    name = "toast"
                    returnType = UnitType
                }
                replaceUnit {
                    AlertDialog.Builder(instance())
                        .setTitle("Hooked")
                        .setMessage("I am hook your toast showing")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
            // 拦截替换方法返回值
            injectMember {
                method {
                    name = "getDataText"
                    returnType = StringType
                }
                replaceTo(prefs.getString(key = "test_data", value = "Test data is nothing"))
            }
        }

        findClass(name = "$packageName.utils.Main").hook {
            // 拦截其他类方法传入形参
            injectMember {
                constructor { param(StringType) }
                beforeHook {
                    // 设置 0 号 param
                    args().set("I am hook constructor param")
                }
            }
        }
    }
}
