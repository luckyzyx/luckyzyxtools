package com.luckyzyx.tools.ui.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.luckyzyx.tools.R;

public class MagiskSwitch extends PreferenceFragmentCompat {

    private static final String PREFERENCE_NAME = "MagiskSettings";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);
        setPreferencesFromResource(R.xml.module_preferences, rootKey);
    }

}