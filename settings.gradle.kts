pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.6.1" apply false
        id("com.android.library") version "8.6.1" apply false
        id("org.jetbrains.kotlin.android") version "1.9.0" apply false
        id("com.google.gms.google-services") version "4.4.2" apply false
        // Add other plugins like Hilt if needed
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Farming"
include(":app")
