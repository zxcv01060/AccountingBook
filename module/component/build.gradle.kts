import tw.idv.louislee.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tw.idv.louislee.component"
    compileSdk = Version.SDK

    defaultConfig {
        minSdk = Version.MIN_SDK
        targetSdk = Version.SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.KOTLIN_COMPIER_EXTENSION
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Version.ANDROID_CORE}")
    implementation("androidx.compose.ui:ui:${Version.COMPOSE}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Version.COMPOSE}")
    implementation("androidx.compose.material3:material3:${Version.MATERIAL_3}")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Version.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Version.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Version.COMPOSE}")
}