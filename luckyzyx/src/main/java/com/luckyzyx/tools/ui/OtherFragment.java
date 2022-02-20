package com.luckyzyx.tools.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;

public class OtherFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //进程管理
        Preference process_management = findPreference("process_management");
        if (process_management != null) {
            process_management.setOnPreferenceClickListener(preference -> {
                Intent process_management_oppo = new Intent().setClassName("com.android.settings", "com.coloros.settings.feature.process.RunningApplicationActivity");
                Intent process_management_oplus = new Intent().setClassName("com.android.settings", "com.oplus.settings.feature.process.RunningApplicationActivity");
                if (requireActivity().getPackageManager().resolveActivity(process_management_oppo, 0) != null) {
                    ShellUtils.execCommand("am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity", true);
                }else if (requireActivity().getPackageManager().resolveActivity(process_management_oplus, 0) != null) {
                    ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true);
                }
                return false;
            });
        }

        //工程模式
        boolean engineermode_oppo = MainActivity.APPexist(requireActivity(),"com.oppo.engineermode");
        boolean engineermode_oplus = MainActivity.APPexist(requireActivity(),"com.oplus.engineermode");
        Preference engineering_model = findPreference("engineering_model");
        if(engineering_model != null) {
            engineering_model.setOnPreferenceClickListener(preference -> {
                if (engineermode_oppo) {
                    ShellUtils.execCommand("am start -n com.oppo.engineermode/.aftersale.AfterSalePage", true);
                }else if (engineermode_oplus) {
                    ShellUtils.execCommand("am start -n com.oplus.engineermode/.aftersale.AfterSalePage", true);
                }
                return false;
            });
        }
        //充电测试
        Preference power_test = findPreference("power_test");
        if(power_test != null) {
            power_test.setOnPreferenceClickListener(preference -> {
                if (engineermode_oppo) {
                    ShellUtils.execCommand("am start -n com.oppo.engineermode/.charge.modeltest.BatteryInfoShow", true);
                }else if (engineermode_oplus) {
                    ShellUtils.execCommand("am start -n com.oplus.engineermode/.charge.modeltest.BatteryInfoShow", true);
                }
                return false;
            });
        }

        //系统界面演示模式
        Preference systemui_demomode = findPreference("systemui_demomode");
        if (systemui_demomode != null){
            systemui_demomode.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.systemui/.DemoMode", true);
                return false;
            });
        }

        //无线调试
        SwitchPreference wifi_adb = findPreference("wifi_adb");
        if (wifi_adb != null) {
            ShellUtils.CommandResult port = ShellUtils.execCommand("getprop service.adb.tcp.port",true,true);
            ShellUtils.CommandResult ip = ShellUtils.execCommand("ifconfig wlan0 | grep 'inet addr' | awk '{ print $2}' | awk -F: '{print $2}' 2>/dev/null",true,true);
            if (!port.successMsg.equals("")){
                wifi_adb.setChecked(true);
                if (!ip.successMsg.equals("")){
                    wifi_adb.setSummary(wifi_adb.getSummary()+"\nadb connect "+ip.successMsg+":"+port.successMsg);
                }else{
                    wifi_adb.setSummary(wifi_adb.getSummary()+"\nadb connect IP:"+port.successMsg);
                }
            }
        }

        //开发者模式
        Preference development = findPreference("development");
        if (development != null) {
            development.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -a com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS", true);
                return false;
            });
        }


    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName("OtherSettings");
        setPreferencesFromResource(R.xml.other_preferences, rootKey);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
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
            boolean oplus_gamespace = MainActivity.APPexist(requireActivity(),"com.oplus.games");
            if (oppo_gamespace){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.coloros.gamespaceui", true);
                else ShellUtils.execCommand("pm enable com.coloros.gamespaceui", true);
            }else if (oplus_gamespace){
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

        if ("touches".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("settings put system show_touches 1", true);
            else ShellUtils.execCommand("settings put system show_touches 0", true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
