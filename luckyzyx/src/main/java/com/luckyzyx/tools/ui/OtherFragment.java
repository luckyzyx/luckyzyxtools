package com.luckyzyx.tools.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;

public class OtherFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName("OtherSettings");
        setPreferencesFromResource(R.xml.other_preferences, rootKey);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        //进程管理
        Preference process_management = findPreference("process_management");
        assert process_management != null;
        process_management.setOnPreferenceClickListener(preference -> {
            Intent process_management_oppo = new Intent().setClassName("com.android.settings", "com.coloros.settings.feature.process.RunningApplicationActivity");
            Intent process_management_oplus = new Intent().setClassName("com.android.settings", "com.oplus.settings.feature.process.RunningApplicationActivity");
            if (requireActivity().getPackageManager().resolveActivity(process_management_oppo, 0) != null) {
                ShellUtils.execCommand("am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity", true);
            }
            if (requireActivity().getPackageManager().resolveActivity(process_management_oplus, 0) != null) {
                ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true);
            }
            return false;
        });

        //工程模式
        boolean engineermode_oppo = MainActivity.APPexist(requireActivity(),"com.oppo.engineermode");
        boolean engineermode_oplus = MainActivity.APPexist(requireActivity(),"com.oplus.engineermode");
        Preference engineering_model = findPreference("engineering_model");
        assert engineering_model != null;
        engineering_model.setOnPreferenceClickListener(preference -> {
            if (engineermode_oppo){
                ShellUtils.execCommand("am start -n com.oppo.engineermode/com.oppo.engineermode.aftersale.AfterSalePage", true);
            }
            if (engineermode_oplus){
                ShellUtils.execCommand("am start -n com.oplus.engineermode/com.oplus.engineermode.aftersale.AfterSalePage", true);
            }
            return false;
        });
        //充电测试
        Preference power_test = findPreference("power_test");
        assert power_test != null;
        power_test.setOnPreferenceClickListener(preference -> {
            if (engineermode_oppo){
                ShellUtils.execCommand("am start -n com.oppo.engineermode/com.oppo.engineermode.charge.modeltest.BatteryInfoShow", true);
            }
            if (engineermode_oplus){
                ShellUtils.execCommand("am start -n com.oplus.engineermode/com.oplus.engineermode.charge.modeltest.BatteryInfoShow", true);
            }
            return false;
        });

        //系统界面演示模式
        Preference systemui_demomode = findPreference("systemui_demomode");
        assert systemui_demomode != null;
        systemui_demomode.setOnPreferenceClickListener(preference -> {
            ShellUtils.execCommand("am start -n com.android.systemui/com.android.systemui.DemoMode", true);
            return false;
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();

        if ("disable_ota".equals(key)) {
            String[] disableota_oppo = new String[] {"pm disable com.oppo.ota >/dev/null","pm disable com.oppo.sau >/dev/null","pm disable com.oppo.sauhelper >/dev/null" };
            String[] enableota_oppo = new String[] {"pm enable com.oppo.ota >/dev/null","pm enable com.oppo.sau >/dev/null","pm enable com.oppo.sauhelper >/dev/null" };
            String[] disableota_oplus = new String[] {"pm disable com.oplus.ota >/dev/null","pm disable com.oplus.sau >/dev/null","pm disable com.oplus.sauhelper >/dev/null" };
            String[] enableota_oplus = new String[] {"pm enable com.oplus.ota >/dev/null","pm enable com.oplus.sau >/dev/null","pm enable com.oplus.sauhelper >/dev/null" };
            boolean oppo_ota = MainActivity.APPexist(requireActivity(),"com.oppo.ota");
            boolean oplus_ota = MainActivity.APPexist(requireActivity(),"com.oplus.ota");
            if (oppo_ota){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota_oppo, true);
                else ShellUtils.execCommand(enableota_oppo, true);
            }
            if (oplus_ota){
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
            }
            if (oplus_gamespace){
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.oplus.games", true);
                else ShellUtils.execCommand("pm enable com.oplus.games", true);
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
