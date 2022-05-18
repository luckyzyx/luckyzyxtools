package com.luckyzyx.statusbarredone.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {

    override fun onInit() {
        configs {
            // 全局调试用的 TAG,在 Logcat 控制台过滤此 TAG 可找到详细日志
            debugTag = "RemoveStatusBarRedOne"
            // 是否开启调试模式,请注意 - 若作为发布版本请务必关闭调试功能防止对用户设备造成大量日志填充
            isDebug = false
            // 是否启用调试日志的输出功能,一旦关闭后除手动日志外 API 将停止全部日志的输出 - 建议不要随意关掉这个选项
            // 虽然说对用户的设备写入大量日志是不正确的 - 但是没有日志你将无法调试
            // 关于日志是否会影响设备的流畅度一直是一个伪命题,但是不设置这个选项可能会引起一些非议 - 建议不要关闭就是了
            isAllowPrintingLogs = true
            // 是否启用 [YukiHookModulePrefs] 的键值缓存功能
            // 若无和模块频繁交互数据在宿主重新启动之前建议开启,若需要实时交互数据建议关闭或从 [YukiHookModulePrefs] 中进行动态配置
            isEnableModulePrefsCache = true
            // 是否启用 [Member] 缓存功能
            // 为防止 [Member] 复用过高造成的系统 GC 问题 - 此功能默认启用
            // 除非缓存的 [Member] 发生了混淆的问题 - 否则建议启用
            isEnableMemberCache = true
        }
    }

    override fun onHook() = encase {
        loadApp(name =  "com.android.systemui"){
            findClass(name = "com.oplusos.systemui.ext.BaseClockExt").hook {
                injectMember {
                    method {
                        name = "init"
                    }
                    afterHook {
                        field {
                            name = "mIsDateTimePanel"
                            type = BooleanType
                        }.get(instance).setFalse()
                    }
                }
            }
        }
    }
}