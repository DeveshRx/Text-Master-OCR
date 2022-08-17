package devesh.app.common.firebase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import devesh.app.common.BuildConfig;
import devesh.app.common.R;


public class RemoteConfig {
    String TAG = "RemoteConfig";
    FirebaseRemoteConfig mFirebaseRemoteConfig;

   final static int TIME_60_MIN_IN_SECONDS=3600;

    public RemoteConfig(Context context) {
        int SEC=TIME_60_MIN_IN_SECONDS;
        if(BuildConfig.DEBUG){
            SEC=60;
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(SEC)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);
                            Log.d(TAG, "onComplete: Fetch and activate succeeded");

                        } else {
                            Log.d(TAG, "onComplete: Fetch failed");

                        }

                    }
                });
    }

    public String getString(String key) {
        return mFirebaseRemoteConfig.getString(key);
    }


    public Boolean getBoolean(String key) {
        return mFirebaseRemoteConfig.getBoolean(key);
    }

    public Boolean isAdsEnabled() {
        //return false;
        return mFirebaseRemoteConfig.getBoolean("PharmaHub_Ads_Enabled");
    }

}
