package devesh.app.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.MuteThisAdListener;
import com.google.android.gms.ads.MuteThisAdReason;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import devesh.app.common.firebase.RemoteConfig;
import devesh.app.common.utils.CachePref;

public class AdMobAPI {
    Context mContext;
    String TAG = "AdMobAPI";
    boolean isAdsEnabled;
    Activity mActivity;
    RemoteConfig remoteConfig;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;

    CachePref cachePref;
    ConsentInformation consentInformation;
    AtomicBoolean isMobileAdsInitializeCalled;

    public AdMobAPI(Activity activity) {
        mContext = activity.getApplicationContext();
        remoteConfig = new RemoteConfig(activity);
        cachePref = new CachePref(activity);

        //  isAdsEnabled = remoteConfig.isAdsEnabled();
        //AppLovinSdk.getInstance(context.getString(R.string.APPLOVIN_SDK_KEY), null, context).initializeSdk();

        //  AppLovinPrivacySettings.setHasUserConsent(true, context);
        boolean isSub = cachePref.getBoolean(activity.getString(R.string.Pref_isSubscribed));
        if (isSub) {
            isAdsEnabled = false;
        } else {
            isAdsEnabled = true;
        }


        if (!BuildConfig.DEBUG) {
            List<String> testDeviceIds = Arrays.asList("4DA615D3074C3B7ED74AF7BC445EB58B");
            RequestConfiguration configuration =
                    new RequestConfiguration.Builder()
                            .setTestDeviceIds(testDeviceIds)
                            .build();


            MobileAds.setRequestConfiguration(configuration);

        }


        isMobileAdsInitializeCalled = new AtomicBoolean(false);

        // Set tag for under age of consent. false means users are not under age
        // of consent.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(activity);
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    // TODO: Load and show the consent form.
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            activity,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });


        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }


    }

    void initializeMobileAdsSdk(){
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


    }

    public void setAdBannerTo(AdView adView) {
        if (isAdsEnabled) {
            AdSize adSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(mContext, 320);

            //    adView.setAdSize(AdSize.SMART_BANNER);
            //    adView.setAdUnitId(mContext.getString(R.string.AdMob_Banner_Id1));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.d(TAG, "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e(TAG, "onAdFailedToLoad: " + adError);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Log.d(TAG, "onAdOpened: ");
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                    Log.d(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                    Log.d(TAG, "onAdClosed: ");
                }
            });

        } else {
            adView.setVisibility(View.GONE);
        }


    }

    public void setAdaptiveBanner(AdView adView, Activity activity) {
        if (isAdsEnabled) {
            AdSize adSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(mContext, 320);

            //    adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(mContext.getString(R.string.AdMob_Banner_Id1));
            adView.setAdSize(getAdSize(activity));
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.d(TAG, "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e(TAG, "onAdFailedToLoad: " + adError);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Log.d(TAG, "onAdOpened: ");
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                    Log.d(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                    Log.d(TAG, "onAdClosed: ");
                }
            });

        } else {
            adView.setVisibility(View.GONE);
        }


    }

    // InterstitialAd

    public void setAdaptiveBanner(FrameLayout adContainerView, Activity activity) {
        if (isAdsEnabled) {
            AdView adView = new AdView(activity);

            //    adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(mContext.getString(R.string.AdMob_Banner_Id1));

            adContainerView.addView(adView);

            adView.setAdSize(getAdSize(activity));

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    Log.d(TAG, "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    Log.e(TAG, "onAdFailedToLoad: " + adError);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                    Log.d(TAG, "onAdOpened: ");
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                    Log.d(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                    Log.d(TAG, "onAdClosed: ");
                }
            });

        } else {
            adContainerView.setVisibility(View.GONE);
        }


    }

    public void LoadInterstitialAd(Activity activity) {
        mActivity = activity;
        if (isAdsEnabled) {
            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(mContext, mContext.getString(R.string.AdMob_Int_Id1), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i(TAG, "onAdLoaded");
                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.d("TAG", "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    Log.d("TAG", "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    mInterstitialAd = null;
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i(TAG, loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });

        }

    }

    public void ShowInterstitialAd() {
        if (isAdsEnabled) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(mActivity);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        }

    }

    // Rewarded Ads

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mContext, adWidth);
    }

    public void InitRewardedAd(Activity activity) {
        mActivity = activity;
        if (isAdsEnabled) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(mContext, mContext.getString(R.string.AdMob_RewardedAds_Id1),
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            Log.d(TAG, "Ad was loaded.");
                            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    Log.d(TAG, "Ad was shown.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when ad fails to show.
                                    Log.d(TAG, "Ad failed to show.");
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    // Set the ad reference to null so you don't show the ad a second time.
                                    Log.d(TAG, "Ad was dismissed.");
                                    mRewardedAd = null;
                                }
                            });
                        }
                    });


        }
    }

    public void ShowRewardedAd() {
        if (isAdsEnabled) {

            if (mRewardedAd != null) {

                mRewardedAd.show(mActivity, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Log.d(TAG, "onUserEarnedReward: rewardAmount: " + rewardAmount + "\n rewardType: " + rewardType);
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }

        }
    }


    // Native Ads
    NativeAd nativeAd;
    AdLoader adLoader;

    public void loadNativeAd(FrameLayout frameLayout, Activity activity) {
        mActivity = activity;
        boolean isSub = cachePref.getBoolean(mContext.getString(R.string.Pref_isSubscribed));
        if (isSub) {
            isAdsEnabled = false;
        } else {
            isAdsEnabled = true;
        }


        if (isAdsEnabled) {


            adLoader = new AdLoader.Builder(mContext, mContext.getString(R.string.AdMob_NativeAd1))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd NativeAd) {
                            // Show the ad.
                            Log.d(TAG, "onNativeAdLoaded: ");
                       /* if (adLoader.isLoading()) {
                            // The AdLoader is still loading ads.
                            // Expect more adLoaded or onAdFailedToLoad callbacks.
                        } else {
                            // The AdLoader has finished loading ads.
                        }*/
                            boolean isDestroyed = false;
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            NativeAd.destroy();
                            return;
                        }*/
                            // You must call destroy on old ads when you are done with them,
                            // otherwise you will have a memory leak.
                            if (nativeAd != null) {
                                nativeAd.destroy();
                            }
                            nativeAd = NativeAd;

                            NativeAdView adView =
                                    (NativeAdView) mActivity.getLayoutInflater()
                                            .inflate(R.layout.nativead1, null);
                            populateUnifiedNativeAdView(NativeAd, adView);

                            nativeAd.setMuteThisAdListener(new MuteThisAdListener() {
                                @Override
                                public void onAdMuted() {
                                    Toast.makeText(mActivity, "Ad muted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            frameLayout.removeAllViews();
                            frameLayout.addView(adView);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            Log.e(TAG, "onAdFailedToLoad: " + adError);
                        }

                        @Override
                        public void onAdClicked() {
                            // Log the click event or other custom behavior.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .setVideoOptions(new VideoOptions.Builder()
                                    .setStartMuted(true)
                                    .build())
                            .setRequestCustomMuteThisAd(true)
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        }
        //adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        if (nativeAd.isCustomMuteThisAdEnabled()) {
            enableCustomMuteWithReasons(nativeAd.getMuteThisAdReasons());
        } else {
            hideCustomMute();
        }
        // Set the media view.
        //adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        // adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);


        MediaView mediaView = adView.findViewById(R.id.ad_media);

        if (nativeAd.getMediaContent() != null) {
            adView.setMediaView(mediaView);

            mediaView.setMediaContent(nativeAd.getMediaContent());
            if (nativeAd.getMediaContent().hasVideoContent()) {
                Log.d(TAG, "populateUnifiedNativeAdView: nativeAd.getMediaContent().hasVideoContent() YES");
            } else {
                Log.d(TAG, "populateUnifiedNativeAdView: nativeAd.getMediaContent().hasVideoContent() NO");
                adView.findViewById(R.id.ad_media).setVisibility(View.GONE);
            }

            Log.d(TAG, "populateUnifiedNativeAdView: nativeAd.getMediaContent()");
        } else {
            Log.d(TAG, "populateUnifiedNativeAdView: nativeAd.getMediaContent() NULL");
        }


        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
  /*      VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    refresh.setEnabled(true);
                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
            videoStatus.setText("Video status: Ad does not contain a video asset.");
            refresh.setEnabled(true);
        }*/
    }

    private void enableCustomMuteWithReasons(List<MuteThisAdReason> reasons) {
        //TODO: This method should show your custom mute button and provide the list
        // of reasons to the interface that are to be displayed when the user mutes
        // the ad.

        Log.d(TAG, "enableCustomMuteWithReasons: ");
    }

    private void hideCustomMute() {
        //TODO: Remove / hide the custom mute button from your user interface.
        Log.d(TAG, "hideCustomMute: ");
    }

    private void muteAdDialogDidSelectReason(MuteThisAdReason reason) {
        // Report the mute action and reason to the ad.
        nativeAd.muteThisAd(reason);
        muteAd();
    }

    private void muteAd() {
        //TODO: Mute / hide the ad in your preferred manner.
    }

    public void DestroyAds() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }

    }


}
