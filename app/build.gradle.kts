import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")
}

val _versionName = "1.0.0"
val _versionCode = 1

android {
    namespace = "com.goodfather.sdk.textook"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.goodfather.sdk.textook"
        minSdk = 23
        targetSdk = 33
        versionCode = _versionCode
        versionName = _versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resourcePrefix = "gbb_sdk_"
    }

    signingConfigs {
        create("release") {
            storeFile = File(rootProject.projectDir,"app/tcl.jks")
            storePassword = "1q2w3e"
            keyAlias = "goodfather"
            keyPassword = "1q2w3e"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = false
            enableV4Signing = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        val kotlinComileArgs = ArrayList<String>().let {
            it += "-Xjvm-default=all"
            it
        }
        freeCompilerArgs += kotlinComileArgs
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        outputs.all {
            if (outputFile.name.endsWith(".aar")) {
                val timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm").format(LocalDateTime.now())
                val fileName = "com.goodfather.sdk.textbook-" + "${buildType.name}-${_versionName}(${_versionCode})-${timestamp}.aar"
                (this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl)?.apply {
                    outputFileName = fileName
                }

            }


        }
    }
}

dependencies {
    val compose = rootProject.extra["compose_version"]
    val compose_ui = rootProject.extra["compose_ui_version"]
    val room_version = rootProject.extra["room_version"]
    val datastore_version = rootProject.extra["datastore_version"]
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:$compose")

    implementation("androidx.compose.ui:ui:$compose_ui")
    implementation("androidx.compose.ui:ui-graphics:$compose_ui")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_ui")
    implementation("androidx.compose.material3:material3:1.1.2")

    implementation("androidx.navigation:navigation-runtime-ktx:2.5.3")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
//    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

//    implementation("com.google.dagger:hilt-android:2.44.2")
//    kapt("com.google.dagger:hilt-android-compiler:2.44.2")

    //room
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //retrofit moshi
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    //moshi used KotlinJsonAdapterFactory
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:$datastore_version")
    implementation("androidx.datastore:datastore-preferences-core:$datastore_version")

    //paging
    implementation("androidx.paging:paging-compose:3.2.1")
    implementation("androidx.paging:paging-runtime:3.2.1")

    implementation("com.gitee.li_yu_jiang:Android_CN_OAID:4.2.8")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")

    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_ui")
}