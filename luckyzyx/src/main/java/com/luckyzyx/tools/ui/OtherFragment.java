package com.luckyzyx.tools.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.SPUtils;
import com.luckyzyx.tools.utils.ShellUtils;

public class OtherFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName("OtherSettings");
        setPreferencesFromResource(R.xml.other_preferences, rootKey);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        if(SPUtils.getString(requireActivity(),"brand",null).equals("OPPO")){
            //移除OnePlus
            getPreferenceScreen().removePreference(findPreference("coloros_oplus"));
            getPreferenceScreen().removePreference(findPreference("developer_oplus"));

            //进程管理_oppo
            Preference process_management_oppo = findPreference("process_management_oppo");
            assert process_management_oppo != null;
            process_management_oppo.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.settings/com.coloros.settings.feature.process.RunningApplicationActivity", true);
                return false;
            });

            //工程模式_oppo
            Preference engineering_model_oppo = findPreference("engineering_model_oppo");
            assert engineering_model_oppo != null;
            engineering_model_oppo.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.oppo.engineermode/com.oppo.engineermode.aftersale.AfterSalePage", true);
                return false;
            });

            //充电测试_oppo
            Preference power_test_oppo = findPreference("power_test_oppo");
            assert power_test_oppo != null;
            power_test_oppo.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.oppo.engineermode/com.oppo.engineermode.charge.modeltest.BatteryInfoShow", true);
                return false;
            });

            //系统界面演示模式_oppo
            Preference systemui_demomode_oppo = findPreference("systemui_demomode_oppo");
            assert systemui_demomode_oppo != null;
            systemui_demomode_oppo.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.systemui/com.android.systemui.DemoMode", true);
                return false;
            });
        }
        if(SPUtils.getString(requireActivity(),"brand",null).equals("OnePlus")){

            //移除OPPO
            getPreferenceScreen().removePreference(findPreference("coloros_oppo"));
            getPreferenceScreen().removePreference(findPreference("developer_oppo"));

            //进程管理_oplus
            Preference process_management_oplus = findPreference("process_management_oplus");
            assert process_management_oplus != null;
            process_management_oplus.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true);
                return false;
            });

            //工程模式_oplus
            Preference engineering_model_oplus = findPreference("engineering_model_oplus");
            assert engineering_model_oplus != null;
            engineering_model_oplus.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.oplus.engineermode/com.oplus.engineermode.aftersale.AfterSalePage", true);
                return false;
            });

            //充电测试_oplus
            Preference power_test_oplus = findPreference("power_test_oplus");
            assert power_test_oplus != null;
            power_test_oplus.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.oplus.engineermode/com.oplus.engineermode.charge.modeltest.BatteryInfoShow", true);
                return false;
            });

            //系统界面演示模式_oplus
            Preference systemui_demomode_oplus = findPreference("systemui_demomode_oplus");
            assert systemui_demomode_oplus != null;
            systemui_demomode_oplus.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.systemui/com.android.systemui.DemoMode", true);
                return false;
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();
        if ("disable_ota_oppo".equals(key)) {
            String[] disableota = new String[] {"pm disable com.oppo.ota >/dev/null","pm disable com.oppo.sau >/dev/null","pm disable com.oppo.sauhelper >/dev/null" };
            String[] enableota = new String[] {"pm enable com.oppo.ota >/dev/null","pm enable com.oppo.sau >/dev/null","pm enable com.oppo.sauhelper >/dev/null" };
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota, true);
            else ShellUtils.execCommand(enableota, true);
        }
        if ("disable_ota_oplus".equals(key)) {
            String[] disableota = new String[] {"pm disable com.oplus.ota >/dev/null","pm disable com.oplus.sau >/dev/null","pm disable com.oplus.sauhelper >/dev/null" };
            String[] enableota = new String[] {"pm enable com.oplus.ota >/dev/null","pm enable com.oplus.sau >/dev/null","pm enable com.oplus.sauhelper >/dev/null" };
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota, true);
            else ShellUtils.execCommand(enableota, true);
        }
        if ("disable_gamespace_oppo".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.coloros.gamespaceui", true);
            else ShellUtils.execCommand("pm enable com.coloros.gamespaceui", true);
        }
        if ("disable_gamespace_oplus".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.oplus.games", true);
            else ShellUtils.execCommand("pm enable com.oplus.games", true);
        }
        if ("touches_oppo".equals(key)) {
            if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("settings put system show_touches 1", true);
            else ShellUtils.execCommand("settings put system show_touches 0", true);
        }
        if ("touches_oplus".equals(key)) {
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
