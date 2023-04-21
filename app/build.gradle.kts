import tw.idv.louislee.Version

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "tw.idv.louislee.accountingbook"
    compileSdk = Version.SDK

    defaultConfig {
        applicationId = "tw.idv.louislee.accountingbook"
        minSdk = Version.MIN_SDK
        targetSdk = Version.SDK
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.KOTLIN_COMPOSE_COMPILER_EXTENSION
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":module:domain")))
    implementation("androidx.core:core-ktx:${Version.ANDROID_CORE}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${Version.LIFECYCLE}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Version.LIFECYCLE}")
    implementation("androidx.activity:activity-compose:${Version.ACTIVITY_COMPOSE}")
    implementation("androidx.compose.ui:ui:${Version.COMPOSE}")
    implementation("androidx.compose.ui:ui-tooling-preview:${Version.COMPOSE}")
    implementation("androidx.compose.material3:material3:${Version.MATERIAL_3}")
    implementation("io.insert-koin:koin-android:${Version.KOIN}")
    implementation("io.insert-koin:koin-androidx-compose:${Version.KOIN_ANDROID_COMPOSE}")
    implementation("io.insert-koin:koin-annotations:${Version.KOIN_KSP}")
    ksp("io.insert-koin:koin-ksp-compiler:${Version.KOIN_KSP}")
    implementation("com.squareup.sqldelight:android-driver:${Version.SQL_DELIGHT}")
    implementation("androidx.navigation:navigation-compose:${Version.NAV}")
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:${Version.SHEETS_COMPOSE_DIALOGS}")
    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:${Version.SHEETS_COMPOSE_DIALOGS}")
    implementation("com.maxkeppeler.sheets-compose-dialogs:clock:${Version.SHEETS_COMPOSE_DIALOGS}")
    implementation("androidx.camera:camera-camera2:${Version.CAMERA_X}")
    implementation("androidx.camera:camera-lifecycle:${Version.CAMERA_X}")
    implementation("androidx.camera:camera-view:${Version.CAMERA_X}")
    implementation("androidx.camera:camera-mlkit-vision:${Version.CAMERA_X_ML_KIT_VISION}")
    implementation("com.google.mlkit:barcode-scanning:${Version.ML_KIT_BARCODE_SCANNING}")
    implementation("com.google.accompanist:accompanist-permissions:${Version.PERMISSION}")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Version.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Version.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Version.COMPOSE}")
}