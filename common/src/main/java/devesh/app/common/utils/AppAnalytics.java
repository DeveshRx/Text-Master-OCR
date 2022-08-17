package devesh.app.common.utils;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import java.util.Map;

import devesh.app.common.BuildConfig;
import devesh.app.common.R;


public class AppAnalytics {
    Context mContext;
    FirebaseAnalytics mFirebaseAnalytics;
    String UserUID = "x";


    public AppAnalytics(Application application,Context context) {
        mContext = context;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);



        AppCenter.configure(application, application.getString(R.string.MS_AppCenter_Key));
        if (AppCenter.isConfigured()) {


            AppCenter.start(Crashes.class);
        }

        if(BuildConfig.DEBUG){
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);

        }



    }

    public void logEvent(String EventName,Map<String, String> properties) {
       if(!BuildConfig.DEBUG){


       }

        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }


        // Firebase Analytics

        mFirebaseAnalytics.logEvent(EventName, bundle);
        if(!UserUID.equals("x")){

            mFirebaseAnalytics.setUserId(UserUID);
            mFirebaseAnalytics.setUserProperty("User_UID",UserUID);

        }
        //App Center
        Analytics.trackEvent(EventName, properties);

    }



   /* public void logEvent(Bundle bundle) {
       // Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "App_Flow");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "HomeScreen");
        mFirebaseAnalytics.logEvent("Indra", bundle);

        //App Center
        Analytics.trackEvent("HomeScreen");

    }*/


    /* public void logEvent(String Key, String Value) {
        // Bundle bundle = new Bundle();
         Bundle bundle = new Bundle();
        bundle.putString(Key,Value);
        mFirebaseAnalytics.logEvent("Indra", bundle);

        //App Center
        Analytics.trackEvent("HomeScreen");

    }*/
}
