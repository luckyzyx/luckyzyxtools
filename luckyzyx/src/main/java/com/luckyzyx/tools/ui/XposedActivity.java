package com.luckyzyx.tools.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.luckyzyx.tools.R;

import java.util.Objects;

public class XposedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.activity_xposed);
        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.xposed_fragment, new XposedFragment())
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
        return false;
    }

    public static class XposedFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("XposedSettings");
            setPreferencesFromResource(R.xml.xposed_preferences, rootKey);

            SwitchPreference network_speed = findPreference("network_speed");
            Objects.requireNonNull(network_speed).setEnabled(false);
            network_speed.setChecked(false);
            SwitchPreference packages = findPreference("package");
            Objects.requireNonNull(packages).setEnabled(false);
            packages.setChecked(false);
//            getPreferenceScreen().removePreference(network_speed);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();
        }
    }

}