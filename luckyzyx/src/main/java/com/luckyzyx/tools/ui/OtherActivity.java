package com.luckyzyx.tools.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.ShellUtils;
import com.luckyzyx.tools.utils.XSPUtils;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new OtherFragment())
                    .commitNow();
        }
    }

    public static class OtherFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("OtherSettings");
            setPreferencesFromResource(R.xml.other_preferences, rootKey);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            //进程管理
            Preference process_management = findPreference("process_management");
            assert process_management != null;
            process_management.setOnPreferenceClickListener(preference -> {
                ShellUtils.execCommand("am start -n com.android.settings/com.oplus.settings.feature.process.RunningApplicationActivity", true);
                return false;
            });
            //触摸操作
            Preference touches = findPreference("touches");
            assert touches != null;
            touches.setDefaultValue(ShellUtils.execCommand("settings get system show_touches",true));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if ("disable_ota".equals(key)) {
                String[] disableota = new String[] {"pm disable com.oppo.ota >/dev/null","pm disable com.oppo.sau >/dev/null","pm disable com.oppo.sauhelper >/dev/null" };
                String[] enableota = new String[] {"pm enable com.oppo.ota >/dev/null","pm enable com.oppo.sau >/dev/null","pm enable com.oppo.sauhelper >/dev/null" };
//              String[] disableota = new String[] {"pm disable com.oplus.ota >/dev/null","pm disable com.oplus.sau >/dev/null","pm disable com.oplus.sauhelper >/dev/null" };
//              String[] enableota = new String[] {"pm enable com.oplus.ota >/dev/null","pm enable com.oplus.sau >/dev/null","pm enable com.oplus.sauhelper >/dev/null" };
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand(disableota, true);
                else ShellUtils.execCommand(enableota, true);
            }
            if ("disable_gamespace".equals(key)) {
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("pm disable com.coloros.gamespaceui", true);
                else ShellUtils.execCommand("pm enable com.coloros.gamespaceui", true);
            }
            if ("touches".equals(key)) {
                if (sharedPreferences.getBoolean(key, false)) ShellUtils.execCommand("settings put system show_touches true", true);
                else ShellUtils.execCommand("settings put system show_touches false", true);
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

}