package com.luckyzyx.tools.hook.packageinstaller

import android.view.View
import android.widget.ScrollView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveInstallAds : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.packageinstaller.oplus.InstallAppProgress").hook {
            injectMember {
                method {
                    name = "initView"
                }
                afterHook {
                    //安全应用推荐广告
                    (field { name = "mSuggestLayoutAScrollView" }.get(instance).self as ScrollView).visibility = View.GONE
                    //前往软件商店安装更多应用
//                    (field { name = "mSuggestLayoutB" }.get(instance).self as LinearLayout).visibility = View.GONE
                }
            }
        }
    }
}