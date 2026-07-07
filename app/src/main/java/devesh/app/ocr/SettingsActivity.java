package devesh.app.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


import devesh.app.ocr.billing.BillingActivity;
import devesh.app.ocr.databinding.SettingsActivityBinding;
import devesh.app.ocr.utils.CachePref;
import devesh.app.ocr.utils.InstallSource;

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
      //  InstallSource installSource;
        CachePref cachePref;
        boolean isSubscribed;
        final String[] LanguageOptionsFull = {"Default (English)", "Devanagari देवनागरी", "Japanese 日本", "Korean 한국인", "Chinese 中國人"};

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//            installSource = new InstallSource(getActivity());
            cachePref = new CachePref(getActivity());

            Preference PrefShareApp = findPreference("sharekey");
            PrefShareApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    String url = "";
                    if (InstallSource.getInstallSource(getActivity()).equals(InstallSource.GOOGLE_PLAY_STORE)) {
                        url = getString(R.string.PLAY_STORE_URL);
                    } else if (InstallSource.getInstallSource(getActivity()).equals(InstallSource.SAMSUNG_APP_STORE)) {
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
                    if (InstallSource.getInstallSource(getActivity()).equals(InstallSource.GOOGLE_PLAY_STORE)) {
                        url = getString(R.string.PLAY_STORE_URL);
                    } else if (InstallSource.getInstallSource(getActivity()).equals(InstallSource.SAMSUNG_APP_STORE)) {
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

            Preference BuyBTN = findPreference("buy");
            isSubscribed = cachePref.getBoolean(getString(R.string.Pref_isSubscribed));
            if (isSubscribed) {
                BuyBTN.setIcon(R.drawable.ic_baseline_favorite_40);
                BuyBTN.setSummary("Thank You for Subscribing");
            }
            BuyBTN.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    Intent intent = new Intent(getActivity(), BillingActivity.class);
                    startActivity(intent);

                    return true;
                }
            });

            String d=cachePref.getString("ocrlang");
            Preference OCRLanguage = findPreference("ocrlang");
            if(d!=null){
                int i=Integer.parseInt(d);
                OCRLanguage.setSummary(LanguageOptionsFull[i]);
            }else{
                OCRLanguage.setSummary(LanguageOptionsFull[0]);
            }

            OCRLanguage.setOnPreferenceChangeListener((preference, newValue) -> {
                int i=Integer.parseInt(newValue.toString());
                preference.setSummary(LanguageOptionsFull[i]);
                return true;
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


            Preference PrefAppUpdate=findPreference("appupdate");
            PrefAppUpdate.setOnPreferenceClickListener(preference -> {
                String url = getString(R.string.PLAY_STORE_URL);

                if(InstallSource.isGalaxyStore(getActivity())){
                    url=getString(R.string.GALAXY_STORE_URL);
                }

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Verify that the intent will resolve to an activity
                startActivity(intent);

                return true;

            });


            /*More Apps*/
if(InstallSource.isGalaxyStore(getActivity())){
findPreference("MoreAppsCategory").setVisible(false);
}else{
    findPreference("MoreAppsCategory").setVisible(true);
    MoreApps();
}


        }



        void MoreApps() {

            findPreference("smsdrive")
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(R.string.MoreApps_SMS_Drive_url);
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
                            String url = getString(R.string.MoreApps_Indra_url);
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
                            String url = getString(R.string.MoreApps_PharmaHub_url);
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
                            String url = getString(R.string.MoreApps_SHT_url);
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
                            String url = getString(R.string.MoreApps_Jinx_url);
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
                            String url = getString(R.string.MoreApps_Muzilla_url);
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
                            String url = getString(R.string.MoreApps_QRLite_url);
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
                            String url = getString(R.string.MoreApps_VirtualLSD_url);
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