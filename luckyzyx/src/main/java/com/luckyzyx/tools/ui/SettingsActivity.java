package com.luckyzyx.tools.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.SPUtils;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.settings_activity);
        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_fragment, new SettingsFragment())
                    .commitNow();
        }
    }

    //创建Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    //标题栏返回事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.openOptionsMenu();
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("Settings");
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();
            if ("theme".equals(key)){
                SPUtils.putString(requireActivity(),"theme",sharedPreferences.getString(key,"default"));
                MainActivity.reStart(requireActivity());
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