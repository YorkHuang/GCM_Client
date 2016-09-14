package com.grtek.user.gcm_client.sip;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.grtek.user.gcm_client.R;

/**
 * Created by user on 2016/9/6.
 */
public class SipSettings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sippreferences);
    }
}
