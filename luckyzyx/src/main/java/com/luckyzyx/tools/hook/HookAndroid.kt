package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerI

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        loggerI(msg = "luckyzyx module is starting!")
    }

}