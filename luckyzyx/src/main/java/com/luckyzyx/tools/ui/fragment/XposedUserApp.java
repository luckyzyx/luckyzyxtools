package com.luckyzyx.tools.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;

public class XposedUserApp extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String PREFERENCE_NAME = "XposedSettings";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
        setPreferencesFromResource(R.xml.xposed_userapp, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(@NonNull Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Toast.makeText(requireActivity(), key+":"+sharedPreferences.getBoolean(key,false), Toast.LENGTH_SHORT).show();
    }
}
