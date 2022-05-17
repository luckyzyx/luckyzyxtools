package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookMoreAnime : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //跳过启动广告页
        if (prefs(PrefsFile).getBoolean("skip_startad", false)){
            findClass(name = "com.east2d.haoduo.ui.activity.SplashActivity").hook {
                injectMember {
                    method {
                        name = "isAdOpen"
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        }

        //VIP 下载原图
        if(prefs(PrefsFile).getBoolean("vip_download", false)){
            findClass(name = "com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity").hook {
                injectMember {
                    method {
                        name = "isVip"
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        }
    }
}
