package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.activity_settings);

        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_fragment, new SettingsFragment())
                    .commitNow();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("Settings");
            setPreferencesFromResource(R.xml.settings_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(@NonNull Preference preference) {
            return super.onPreferenceTreeClick(preference);
        }
    }
}