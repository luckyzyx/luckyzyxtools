package com.luckyzyx.tools.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.BuildConfig;
import com.luckyzyx.tools.R;
import com.luckyzyx.tools.utils.Log;

public class XposedActivity extends AppCompatActivity {

    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        CheckXposed();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new XposedFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressLint("WorldReadableFiles")
    private void CheckXposed() {
        try {
            getSharedPreferences("XposedSettings", Context.MODE_WORLD_READABLE);
        } catch (SecurityException exception) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.not_supported))
                    .setPositiveButton(android.R.string.ok, (dialog12, which) -> finish())
                    .setNegativeButton(R.string.ignore, null)
                    .show();
        }
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

    public static class XposedFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("XposedSettings");
            setPreferencesFromResource(R.xml.xposed_preferences, rootKey);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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