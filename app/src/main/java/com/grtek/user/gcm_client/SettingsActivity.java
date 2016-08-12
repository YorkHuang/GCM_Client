package com.grtek.user.gcm_client;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by user on 2016/8/8.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key) {

            case "pref_key_notification":
                Preference pref = findPreference(key);
                if (pref.getSharedPreferences().getBoolean("pref_key_notification", true)) {
                    pref.setSummary(sharedPreferences.getString(key, getResources().getString(R.string.pref_summary_auto_notify_on)));
                } else {
                    pref.setSummary(sharedPreferences.getString(key, getResources().getString(R.string.pref_summary_auto_notify_off)));
                }
                break;

        }

    }
}
