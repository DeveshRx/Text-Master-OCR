package devesh.app.ocr.utils;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class CachePref {

    //Context mContext;
    final static String TAG = "CachePref: ";
    SharedPreferences sharedPref;
    Context mActivity;

    public CachePref(Context activity) {
        //mContext=context;
        mActivity = activity;
        //sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
    }


    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPref.getString(key, null);
    }


    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sharedPref.getBoolean(key, false);
    }


    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return sharedPref.getInt(key, 0);
    }



    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return sharedPref.getLong(key, 0);
    }


    public void delete(String key) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

   /* public String getTheme() {
      return sharedPref.getString(mActivity.getString(R.string.Pref_ThemeMode),SYSTEM_DEFAULT);
    }*/

  /* public void updateTheme(String mode){
        if(mode.equals(AppThemePreferences.SYSTEM_DEFAULT)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            Log.d(TAG, "updateTheme: MODE_NIGHT_FOLLOW_SYSTEM Applied");
        }else if(mode.equals(AppThemePreferences.LIGHT_MODE)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.d(TAG, "updateTheme: MODE_NIGHT_NO applied");
        }else if(mode.equals(AppThemePreferences.DARK_MODE)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Log.d(TAG, "updateTheme: MODE_NIGHT_YES applied");
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            Log.d(TAG, "updateTheme: MODE_NIGHT_FOLLOW_SYSTEM applied #");
        }
    }
*/

   /* public void setThemePref(String s){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mActivity.getString(R.string.Pref_ThemeMode), s);
        editor.apply();
    } */

}
