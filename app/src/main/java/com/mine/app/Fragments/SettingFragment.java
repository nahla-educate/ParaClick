package com.mine.app.Fragments;


import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.mine.app.R;


public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);

    }
}