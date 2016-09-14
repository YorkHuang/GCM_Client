package com.grtek.user.gcm_client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.grtek.user.gcm_client.gcm.QuickstartPreferences;
import com.grtek.user.gcm_client.gcm.RegistrationIntentService;
import com.grtek.user.gcm_client.http.JSONParser;
import com.grtek.user.gcm_client.http.URLParameter;
import com.grtek.user.gcm_client.sip.SIPActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/8/5.
 */
public class MainActivity extends AppCompatActivity  {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int Handler_POSTDELAY_TIME = 10000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private RecyclerView mRCV;
    private RecycleViewAdapter mRCVAdapter;
    private List<EmgUnit> mEmgList = new ArrayList<EmgUnit>();
    private Handler mHandler = new Handler();
    private ProgressBar mProgress = null;
    private boolean isShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                } else {

                }
            }
        };


        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        mRCV = (RecyclerView) findViewById(R.id.recyclerView);
        mRCV.setLayoutManager(new LinearLayoutManager(this));
        mRCVAdapter = new RecycleViewAdapter(this,mEmgList);
        mRCV.setAdapter(mRCVAdapter);
        mRCVAdapter.setmOnItemClickListener(new RecycleViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

            }
        });

        mProgress = (ProgressBar) findViewById(R.id.progressBar);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mEmgList.clear();
        mRCVAdapter.notifyDataSetChanged();
        mProgress.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 2000);
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (uiHandler != null)
            uiHandler = null;
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sip:
                Intent iSip = new Intent(this, SIPActivity.class);
                startActivity(iSip);
                return true;

            case R.id.action_settings:
                Intent iSetting = new Intent(this, SettingsActivity.class);
                startActivity(iSetting);
                return true;

            case R.id.action_logout:

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putBoolean(QuickstartPreferences.PREF_LOGIN, false).commit();
                sp.edit().putString(QuickstartPreferences.USERID, "");

                Intent intent = new Intent(this, LoginActivity.class);
                startService(intent);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            getEmergencyStreeLightsInfo();

            mHandler.postDelayed(this, Handler_POSTDELAY_TIME);
        }
    };


    private void getEmergencyStreeLightsInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONParser jParser = new JSONParser();
                URLParameter param = new URLParameter();

                JSONObject msg = jParser.makeHttpRequest(
                        "lib/lib_app_socket.php",
                        "POST",
                        param.getEmergencyStreeLightInfo()
                );
                mEmgList.clear();
                try {
                    if (msg != null) {
                        JSONArray list = msg.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {

                            JSONObject obj = list.getJSONObject(i);

                            EmgUnit emg = new EmgUnit(
                                    obj.getString("MAC"),
                                    obj.getString("DATE"),
                                    Double.parseDouble(obj.getString("LAT")),
                                    Double.parseDouble(obj.getString("LNG")),
                                    obj.getString("CAMERAIP")
                            );

                            mEmgList.add(emg);
                        }

                        uiHandler.sendMessage(uiHandler.obtainMessage(0));

                    } else {
                        uiHandler.sendMessage(uiHandler.obtainMessage(1));

                    }
                } catch (JSONException e) {
                    Log.e(TAG, "e: " + e.toString());
                }

            }
        }).start();

    }

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    mRCVAdapter.notifyDataSetChanged();
                    mProgress.setVisibility(View.GONE);
                    break;
                case 1:
                    if (!isShowDialog)
                        showDialog();
                    break;
            }

        }
    };

    private void showDialog(){

        isShowDialog = true;


        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShowDialog = false;
                        mProgress.setVisibility(View.GONE);
                        if (mHandler != null && mRunnable != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShowDialog = false;
                    }
                })
                .show();

    }

}
