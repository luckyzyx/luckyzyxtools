package com.luckyzyx.tools.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luckyzyx.tools.R;

import java.util.Objects;

public class XposedAndroid extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String PREFERENCE_NAME = "XposedSettings";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
        setPreferencesFromResource(R.xml.xposed_android, rootKey);
        Objects.requireNonNull(getPreferenceScreen().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();
        if (key.equals("UsePreSig") && sharedPreferences.getBoolean(key,false)) {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setMessage(R.string.usepresig_warn)
                    .setPositiveButton("OK",null)
                    .show();
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
}
