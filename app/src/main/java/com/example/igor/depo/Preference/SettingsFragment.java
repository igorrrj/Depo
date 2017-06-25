package com.example.igor.depo.Preference;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import com.example.igor.depo.R;

/**
 * Created by Igor on 27.05.2017.
 */

public class SettingsFragment extends PreferenceFragment {
    SwitchPreference switchPreference;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        sharedPreferences=getActivity().getSharedPreferences("wifi_pref", Context.MODE_PRIVATE);


        switchPreference=(SwitchPreference)findPreference("auto_wifi_pref");
        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                WifiManager wifiManager = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
                if(switchPreference.isEnabled())
                {
                    sharedPreferences.edit().putBoolean("wifi",true).apply();
                    wifiManager.setWifiEnabled(true);
                }
                else
                {
                    sharedPreferences.edit().putBoolean("wifi",false).apply();
                        wifiManager.setWifiEnabled(false);
                }
                return true;
            }
        });

    }
}
