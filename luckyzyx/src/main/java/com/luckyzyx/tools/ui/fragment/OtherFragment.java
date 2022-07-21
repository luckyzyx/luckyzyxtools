package com.luckyzyx.tools.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.joom.paranoid.Obfuscate;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.ui.MainActivity;
import com.luckyzyx.tools.utils.SPUtils;
import com.luckyzyx.tools.utils.ShellUtils;

import java.util.Objects;

public class OtherFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String PREFERENCE_NAME = "OtherSettings";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
        setPreferencesFromResource(R.xml.other_preferences, rootKey);
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);

        //判断无线调试状态
        SwitchPreference wifi_adb = findPreference("wifi_adb");
        if (wifi_adb != null && SPUtils.getBoolean(requireActivity(),PREFERENCE_NAME,"wifi_adb")) {
            ShellUtils.CommandResult port = ShellUtils.execCommand("getprop service.adb.tcp.port",true,true);
            ShellUtils.CommandResult ip = ShellUtils.execCommand("ifconfig wlan0 | grep 'inet addr' | awk '{ print $2}' | awk -F: '{print $2}' 2>/dev/null",true,true);
            if (!port.successMsg.equals("")){
                wifi_adb.setChecked(true);
                if (!ip.successMsg.equals("")){
                    wifi_adb.setSummary(wifi_adb.getSummary()+"\nadb connect "+ip.successMsg+":"+port.successMsg);
                }else{
                    wifi_adb.setSummary(wifi_adb.getSummary()+"\nadb connect IP:"+port.successMsg);
                }
            }else{
                wifi_adb.setChecked(false);
            }
        }

        //判断freezer状态
        ShellUtils.CommandResult freezerStatus = ShellUtils.execCommand("settings get global cached_apps_freezer",true,true);
        ListPreference freezer_cached_apps = findPreference("freezer_cached_apps");
        if (freezer_cached_apps != null) {
            switch (freezerStatus.successMsg){
                case "device_default":
                    freezer_cached_apps.setValueIndex(0);
                    if (!SPUtils.getString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps").equals("device_default"))
                        SPUtils.putString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps","device_default");
                    break;
                case "enabled":
                    freezer_cached_apps.setValueIndex(1);
                    if (!SPUtils.getString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps").equals("device_default"))
                        SPUtils.putString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps","enabled");
                    break;
                case "disabled":
                    freezer_cached_apps.setValueIndex(2);
                    if (!SPUtils.getString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps").equals("device_default"))
                        SPUtils.putString(requireActivity(),PREFERENCE_NAME,"freezer_cached_apps","disabled");
                    break;
                default:
                    Toast.makeText(requireActivity(), "freezerStatus:"+freezerStatus.successMsg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        //开发者模式
        if (preference.getKey().equals("development")) {
            ShellUtils.execCommand("am start -a com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS", true);
        }

        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();

        if ("disable_ota".equals(key)) {
            String[] disableota_oppo = {"pm disable com.oppo.ota >/dev/null","pm disable com.oppo.sau >/dev/null","pm disable com.oppo.sauhelper >/dev/null" };
            String[] enableota_oppo = {"pm enable com.oppo.ota >/dev/null","pm enable com.oppo.sau >/dev/null","pm enable com.oppo.sauhelper >/dev/null" };
            String[] disableota_oplus = {"pm disable com.oplus.ota >/dev/null","pm disable com.oplus.sau >/dev/null","pm disable com.oplus.sauhelper >/dev/null" };
            String[] enableota_oplus = {"pm enable com.oplus.ota >/dev/null","pm enable com.oplus.sau >/dev/null","pm enable com.oplus.sauhelper >/dev/null" };
            boolean oppo_ota = MainActivity.APPexist(requireActivity(),"com.oppo.ota");
            boolean oplus_ota = MainActivity.APPexist(requireActivity(),"com.oplus.ota");
            if (oppo_ota){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota_oppo, true);
                else ShellUtils.execCommand(enableota_oppo, true);
            }else if (oplus_ota){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota_oplus, true);
                else ShellUtils.execCommand(enableota_oplus, true);
            }
        }

        if ("disable_gamespace".equals(key)) {
            boolean oppo_gamespace = MainActivity.APPexist(requireActivity(),"com.coloros.gamespaceui");
//            boolean oplus_gamespace = MainActivity.APPexist(requireActivity(),"com.oplus.games");
            if (oppo_gamespace){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.coloros.gamespaceui", true);
                else ShellUtils.execCommand("pm enable com.coloros.gamespaceui", true);
            } else {
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.oplus.games", true);
                else ShellUtils.execCommand("pm enable com.oplus.games", true);
            }
        }

        if ("wifi_adb".equals(key)) {
            String[] adb_start = {"setprop service.adb.tcp.port 6666","stop adbd","killall -9 adbd 2>/dev/null","start adbd"};
            String[] adb_stop = {"setprop service.adb.tcp.port -1","stop adbd","killall -9 adbd 2>/dev/null","start adbd","setprop service.adb.tcp.port ''"};
            if (sharedPreferences.getBoolean(key, false)) {
                ShellUtils.execCommand(adb_start, true);
            } else{
                ShellUtils.execCommand(adb_stop, true);
            }
        }

        if ("freezer_cached_apps".equals(key)) {
            switch (sharedPreferences.getString(key, "device_default")){
                case "device_default":
                    ShellUtils.execCommand("settings put global cached_apps_freezer device_default", true);
                    break;
                case "enabled":
                    ShellUtils.execCommand("settings put global cached_apps_freezer enabled", true);
                    break;
                case "disabled":
                    ShellUtils.execCommand("settings put global cached_apps_freezer disabled", true);
                    break;
            }
        }

        if ("clock_seconds".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("settings put secure clock_seconds 1", true);
            else ShellUtils.execCommand("settings put secure clock_seconds 0", true);
        }

        if ("show_fps".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 1", true);
            else ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 0", true);
        }

        if ("show_touches".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("settings put system show_touches 1", true);
            else ShellUtils.execCommand("settings put system show_touches 0", true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }

    //快捷入口页面Fragment
    @Keep
    @Obfuscate
    public static class QuickEntranceFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
            setPreferencesFromResource(R.xml.quick_entrance_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(@NonNull Preference preference) {
            //系统界面演示模式
            if (preference.getKey().equals("systemui_demomode")){
                ShellUtils.execCommand("am start -n com.android.systemui/.DemoMode", true);
            }
            //进程管理
            if (preference.getKey().equals("process_management")) {
                Intent process_management_oppo = new Intent().setClassName("com.android.settings", "com.coloros.settings.feature.process.RunningApplicationActivity");
                Intent process_management_oplus = new Intent().setClassName("com.android.settings", "com.oplus.settings.feature.process.RunningApplicationActivity");
                if (requireActivity().getPackageManager().resolveActivity(process_management_oppo, 0) != null) {
                    ShellUtils.execCommand("am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity", true);
                }else if (requireActivity().getPackageManager().resolveActivity(process_management_oplus, 0) != null) {
                    ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true);
                }
            }
            //工程模式
            boolean engineermode_oppo = MainActivity.APPexist(requireActivity(),"com.oppo.engineermode");
            boolean engineermode_oplus = MainActivity.APPexist(requireActivity(),"com.oplus.engineermode");
            if(preference.getKey().equals("engineering_model")) {
                if (engineermode_oppo) {
                    ShellUtils.execCommand("am start -n com.oppo.engineermode/.aftersale.AfterSalePage", true);
                }else if (engineermode_oplus) {
                    ShellUtils.execCommand("am start -n com.oplus.engineermode/.aftersale.AfterSalePage", true);
                }
            }
            //充电测试
            if(preference.getKey().equals("power_test")) {
                if (engineermode_oppo) {
                    ShellUtils.execCommand("am start -n com.oppo.engineermode/.charge.modeltest.BatteryInfoShow", true);
                }else if (engineermode_oplus) {
                    ShellUtils.execCommand("am start -n com.oplus.engineermode/.charge.modeltest.BatteryInfoShow", true);
                }
            }
            //反馈工具箱
            if (preference.getKey().equals("logkit")){
                ShellUtils.execCommand("am start -n com.oplus.logkit/.activity.MainActivity", true);
            }
            //游戏助手
            if (preference.getKey().equals("gamespace")) {
                ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true);
            }
            //游戏助手开发者选项
            if (preference.getKey().equals("gamespace_devmode")) {
                ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameDevelopOptionsActivity", true);
            }

            return super.onPreferenceTreeClick(preference);
        }
    }
}
