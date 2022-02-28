package com.luckyzyx.tools.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.CheckTheme(this);
        setContentView(R.layout.about_activity);
        //设置Toolbar
        setSupportActionBar(findViewById(R.id.topAppBar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.about_fragment, new AboutFragment())
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

    public static class AboutFragment extends PreferenceFragmentCompat{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("Settings");
            setPreferencesFromResource(R.xml.about_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {

            return super.onPreferenceTreeClick(preference);
        }
    }

}