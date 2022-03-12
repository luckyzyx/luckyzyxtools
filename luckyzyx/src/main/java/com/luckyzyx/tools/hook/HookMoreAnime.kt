package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookMoreAnime {

    class HookAdOpen : YukiBaseHooker() {

        override fun onHook() {
            findClass(name = "com.east2d.haoduo.ui.activity.SplashActivity").hook {
                // 替换返回值
                injectMember {
                    method {
                        name = "isAdOpen"
                        returnType = BooleanType
                    }
                    replaceTo(true)
                }
            }

        }
    }

    class HookIsVip : YukiBaseHooker() {

        override fun onHook() {
            findClass(name = "com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity").hook {
                // 替换返回值
                injectMember {
                    method {
                        name = "isVip"
                        returnType = BooleanType
                    }
                    replaceTo(true)
                }
            }

        }
    }

}
