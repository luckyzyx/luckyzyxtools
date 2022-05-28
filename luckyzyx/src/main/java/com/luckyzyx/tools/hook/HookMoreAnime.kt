package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.moreanime.SkipStartupPage
import com.luckyzyx.tools.hook.moreanime.VipDownload

class HookMoreAnime : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //跳过启动广告页
        if (prefs(PrefsFile).getBoolean("skip_startup_page", false)) loadHooker(SkipStartupPage())

        //VIP 下载原图
        if(prefs(PrefsFile).getBoolean("vip_download", false)) loadHooker(VipDownload())
    }
}
