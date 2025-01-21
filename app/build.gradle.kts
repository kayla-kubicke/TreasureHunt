plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
   // id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.treasurehunt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.treasurehunt"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Google sign in sdk dependency must use () and ""
    implementation(libs.play.services.auth)
    // adding the dependency for the Recycle view
    implementation(libs.androidx.recyclerview)
    // adding card view dependency
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.camera.core)
    //testing dependency
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //camera x dependecy
    // CameraX core library
    implementation(libs.androidx.camera.core.v150apha05)
    implementation(libs.androidx.camera.lifecycle.v150aplha05)
    implementation(libs.androidx.camera.view.v150alpha05)
    implementation(libs.androidx.camera.extensions.v150alpha05) // Compatible version
    implementation(libs.androidx.camera.camera2.v150alpha05)

    implementation(libs.androidx.core.ktx.v1100)
    implementation(libs.androidx.appcompat.v170)

    //api dependency
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
//    implementation(libs.google.cloud.vision)
//    implementation(libs.gson)
   //firebase bom dependency
   // implementation(platform(libs.firebase.bom))
    implementation(platform(libs.firebase.bom))
    //firebase dependency
    // ML Kit image labeling
    implementation(libs.image.labeling)
    implementation(libs.firebase.analytics)
// If you need custom models for labeling
    implementation(libs.image.labeling.custom)
}