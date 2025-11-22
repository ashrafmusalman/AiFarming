// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.9")
        classpath("com.google.gms:google-services:4.4.2")
    }
    repositories {
        google()
        mavenCentral()
    }
}

// This plugins block is for applying plugins to submodules.
// The "apply false" means that these plugins are not automatically applied
// but are available to be used in your module-level build.gradle files.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

// You can also add additional configuration here if needed.
// For example, you can configure repositories or common properties for all modules.
