import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

    // Add the App Distribution Gradle plugin
    id("com.google.firebase.appdistribution")
}

val AppVersionCode = 9
val AppVersionName = "1.0.0.9"

val keySignFile= gradleLocalProperties(rootDir, providers)
    .getProperty("keySignFile", "")

val keySignPassword= gradleLocalProperties(rootDir, providers)
    .getProperty("keySignPassword", "")

val keySignAlias= gradleLocalProperties(rootDir, providers)
    .getProperty("keySignAlias", "")

val keySignAliasPassword= gradleLocalProperties(rootDir, providers)
    .getProperty("keySignAliasPassword", "")

val AdMobAppID= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobAppID", "")

val AdMobBannerAd1= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobBannerAd1", "")

val AdMobBannerAd2= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobBannerAd2", "")

val AdMobIntAd= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobIntAd", "")

val AdMobRewardedAd= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobRewardedAd", "")

val AdMobNativeAds= gradleLocalProperties(rootDir, providers)
    .getProperty("AdMobNativeAds", "")




android {
    signingConfigs {
        create("release") {
            storeFile = file(keySignFile)
            storePassword = keySignPassword
            keyAlias = keySignAlias
            keyPassword = keySignAliasPassword
        }
    }
    namespace = "devesh.app.ocr"
    compileSdk = 35

    defaultConfig {
        applicationId = "devesh.app.ocr"
        minSdk = 21 //24
        targetSdk = 35
        versionCode = AppVersionCode
        versionName = AppVersionName

        resValue("string", "app_version_code", "\"" + AppVersionCode + "\"")
        multiDexEnabled = true



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }

    buildTypes {

        debug {
            resValue("string", "app_version_name", "\"" + AppVersionName + "\"")

            resValue ("bool", "FirebaseAnalytics_Enabled", "false")
            resValue ("bool", "FirebaseAnalytics_Deactivated", "true")

            isDebuggable = true

            resValue("string", "AdMob_App_Id", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "AdMob_Int_Id1", "ca-app-pub-3940256099942544/1033173712")
            resValue ("string", "AdMob_Banner_Id1", "ca-app-pub-3940256099942544/6300978111")
            resValue ("string", "AdMob_Banner_Id2", "ca-app-pub-3940256099942544/6300978111")
            resValue ("string", "AdMob_RewardedAds_Id1", "ca-app-pub-3940256099942544/5224354917")
            resValue ("string", "AdMob_NativeAd1", "ca-app-pub-3940256099942544/2247696110")

            resValue ("string", "MS_AppCenter_Key", "\"" + "xxxx" + "\"")

        }
        create("internal") {

            resValue("string", "app_version_name", "\"" + AppVersionName + " Internal" + "\"")

            // initWith debug
            resValue( "bool", "FirebaseAnalytics_Enabled", "false")
            resValue("bool", "FirebaseAnalytics_Deactivated", "true")

            versionNameSuffix = " internal"
            isMinifyEnabled = true

            resValue("string", "AdMob_App_Id", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "AdMob_Int_Id1", "ca-app-pub-3940256099942544/1033173710")
            resValue ("string", "AdMob_Banner_Id1", "ca-app-pub-3940256099942544/6300978110")
            resValue ("string", "AdMob_Banner_Id2", "ca-app-pub-3940256099942544/6300978110")
            resValue ("string", "AdMob_RewardedAds_Id1", "ca-app-pub-3940256099942544/5224354910")
            resValue ("string", "AdMob_NativeAd1", "ca-app-pub-3940256099942544/2247696110")

            resValue ("string", "MS_AppCenter_Key", "\"" + "xxxx" + "\"")

            signingConfig = signingConfigs.getByName("release")

            firebaseAppDistribution {
                releaseNotes = "Version: $AppVersionName (internal)\nBuild no: $AppVersionCode"
                groups = "testers"
            }


        }


        release {
            isMinifyEnabled = true

            resValue("string", "app_version_name", "\"" + AppVersionName + "\"")

            resValue("string", "AdMob_App_Id", "$AdMobAppID")
            resValue ("string", "AdMob_Int_Id1", "$AdMobIntAd")
            resValue ("string", "AdMob_Banner_Id1", "$AdMobBannerAd1")
            resValue ("string", "AdMob_Banner_Id2", "$AdMobBannerAd2")
            resValue ("string", "AdMob_RewardedAds_Id1", "$AdMobRewardedAd")
            resValue ("string", "AdMob_NativeAd1", "$AdMobNativeAds")

            resValue("bool", "FirebaseAnalytics_Enabled", "true")
            resValue("bool", "FirebaseAnalytics_Deactivated", "false")


            resValue ("string", "MS_AppCenter_Key", "\"" + "xxxx" + "\"")
            signingConfig = signingConfigs.getByName("release")

            firebaseAppDistribution {
                releaseNotes = "PlayStore \nVersion: $AppVersionName \nBuild no: $AppVersionCode"
                groups = "testers"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.analytics)
    implementation(project(":cropper"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.multidex)

// Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.play.services.ads)
    implementation(libs.firebase.perf)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.config)


    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.video)

    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    //implementation "com.google.android.gms:play-services-mlkit-text-recognition:$project.MlkitTextRecognition_version"
// To recognize Latin script
    implementation(libs.text.recognition)

    // To recognize Chinese script
    implementation(libs.text.recognition.chinese)

    // To recognize Devanagari script
    implementation(libs.text.recognition.devanagari)

    // To recognize Japanese script
    implementation(libs.text.recognition.japanese)

    // To recognize Korean script
    implementation(libs.text.recognition.korean)


    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    // optional - RxJava2 support for Room
    //implementation "androidx.room:room-rxjava2:$project.room_version"

    // optional - RxJava3 support for Room
    //implementation "androidx.room:room-rxjava3:$project.room_version"

    // optional - Guava support for Room, including Optional and ListenableFuture
    //implementation "androidx.room:room-guava:$project.room_version"

    // optional - Test helpers
   // testImplementation "androidx.room:room-testing:$project.room_version"

    // optional - Paging 3 Integration
   // implementation "androidx.room:room-paging:$project.room_version"


    //Google Play Core
    //noinspection RiskyLibrary
    //implementation(libs.core)

    // Play in-app update
     implementation(libs.app.update)
    implementation(libs.review.ktx)


    //MS App Center
   // implementation "com.microsoft.appcenter:appcenter-analytics:$project.MSAppCenter_version"
   // implementation "com.microsoft.appcenter:appcenter-crashes:$project.MSAppCenter_version"


    implementation(libs.gson)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Image Cropper
    // implementation 'com.theartofdev.edmodo:android-image-cropper:+'

    //  def billing_version = "5.2.0"

    implementation(libs.billing)
    implementation(libs.play.services.base)

    implementation(libs.user.messaging.platform)

}