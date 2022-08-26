package devesh.app.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import devesh.app.common.AdMobAPI;
import devesh.app.common.utils.InstallSource;
import devesh.app.moreapps.MoreAppsListFragment;
import devesh.app.ocr.databinding.SettingsActivityBinding;
import devesh.app.user_guide.UserGuideActivity;

public class SettingsActivity extends AppCompatActivity {
    SettingsActivityBinding binding;
    AdMobAPI adMobAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adMobAPI = new AdMobAPI(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.settings.getId(), new SettingsFragment())
                    .commit();


        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        adMobAPI.setAdaptiveBanner(binding.AdFrameLayout, this);
    }




    public static class SettingsFragment extends PreferenceFragmentCompat {
        String TAG = "settings";
        InstallSource installSource;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            installSource = new InstallSource(getActivity());

            Preference PrefShareApp = findPreference("sharekey");
            PrefShareApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    String url = "";
                    if (installSource.getInstallSource().equals(installSource.GOOGLE_PLAY_STORE)) {
                        url = getString(R.string.PLAY_STORE_URL);
                    } else if (installSource.getInstallSource().equals(installSource.SAMSUNG_APP_STORE)) {
                        url = getString(R.string.GALAXY_STORE_URL);
                    } else {
                        url = getString(R.string.PLAY_STORE_URL);
                    }

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " " + url);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                    return true;
                }
            });


            Preference PrefRateApp = findPreference("ratekey");
            PrefRateApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    String url = "";
                    if (installSource.getInstallSource().equals(installSource.GOOGLE_PLAY_STORE)) {
                        url = getString(R.string.PLAY_STORE_URL);
                    } else if (installSource.getInstallSource().equals(installSource.SAMSUNG_APP_STORE)) {
                        url = getString(R.string.GALAXY_STORE_URL);
                    } else {
                        url = getString(R.string.PLAY_STORE_URL);
                    }
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    // Verify that the intent will resolve to an activity
                    startActivity(intent);

                    return true;
                }
            });


            Preference SecPri = findPreference("secpri");
SecPri.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
    public boolean onPreferenceClick(Preference preference) {
        Log.d(TAG, "onPreferenceClick: ");
        String url = "https://www.ephrine.in/privacy-policy";

         Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Verify that the intent will resolve to an activity
        startActivity(intent);

        return true;
    }
});

       /*     Preference PrefGuideApp = findPreference("guide");
            PrefGuideApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    Intent intent = new Intent(getActivity(), UserGuideActivity.class);
                    startActivity(intent);

                    return true;
                }
            });
*/


            /*More Apps*/

            MoreApps();

        }

        void MoreApps(){

            findPreference("smsdrive")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_SMS_Drive_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("indra")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Indra_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });


            findPreference("pharmahub")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_PharmaHub_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("sht")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_SHT_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("jinx")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Jinx_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("muzilla")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Muzilla_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("qrlite")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_QRLite_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });

            findPreference("vlsd")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(devesh.app.moreapps.R.string.MoreApps_VirtualLSD_url);
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                        }
                    });


        }
    }
}