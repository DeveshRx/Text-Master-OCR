// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false

    id("com.google.firebase.crashlytics") version "3.0.2" apply false
// Add the dependency for the App Distribution Gradle plugin
    id("com.google.firebase.appdistribution") version "5.0.0" apply false
}


