package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.systemui.*

class HookSystemUI : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //5752f55->9RT12.1

        //移除锁屏时钟红1
        if (prefs(PrefsFile).getBoolean("remove_lock_screen_redone",false)) loadHooker(RemoveLockScreenRedOne())

        //设置状态栏网速刷新率
        if (prefs(PrefsFile).getBoolean("network_speed",false)) loadHooker(NetworkSpeed())

        //移除充电完成通知
        if (prefs(PrefsFile).getBoolean("remove_charging_completed",false)) loadHooker(RemoveChargingCompleted())

        //移除下拉状态栏时钟红1
        if (prefs(PrefsFile).getBoolean("remove_statusbar_clock_redone",false)) loadHooker(RemoveStatusBarClockRedOne())

        //设置下拉状态栏时钟显秒
        if (prefs(PrefsFile).getBoolean("statusbar_clock_show_second",false)) loadHooker(StatusBarClockShowSecond())

        //移除状态栏开发者选项警告
        if (prefs(PrefsFile).getBoolean("remove_statusbar_devmode",false)) loadHooker(RemoveStatusBarDevMode())

        //移除下拉状态栏底部网络警告
        if (prefs(PrefsFile).getBoolean("remove_statusbar_bottom_networkwarn",false)) loadHooker(RemoveStatusBarBottomNetworkWarn())

        //移除状态栏支付保护图标
        if (prefs(PrefsFile).getBoolean("remove_statusbar_securepayment_icon",false)) loadHooker(RemoveStatusBarSecurePayment())
    }
}
