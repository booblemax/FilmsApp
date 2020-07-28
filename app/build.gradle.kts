plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {

    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion = Apps.buildToolVersion

    defaultConfig {
        applicationId = Apps.appId
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = Classpath.testInstrumentalRunner
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "TOKEN", project.property("token") as String)
            buildConfigField("String", "BASE_URL", project.property("baseurl") as String)
            buildConfigField("String", "FULL_IMAGE_URL", project.property("fullImageUrl") as String)
            buildConfigField("String", "REDUCED_IMAGE_URL", project.property("reducedImageUrl") as String)
            buildConfigField("String", "GOOGLE_API_KEY", project.property("googleApiKey") as String)
        }
        getByName("release") {
            buildConfigField("String", "TOKEN", project.property("token") as String)
            buildConfigField("String", "BASE_URL", project.property("baseurl") as String)
            buildConfigField("String", "FULL_IMAGE_URL", project.property("fullImageUrl") as String)
            buildConfigField("String", "REDUCED_IMAGE_URL", project.property("reducedImageUrl") as String)
            buildConfigField("String", "GOOGLE_API_KEY", project.property("googleApiKey") as String)
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        dataBinding {
            isEnabledForTests = true
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.appcompat)
    implementation(Libs.coroutines)
    implementation(Libs.core_ktx)
    implementation(Libs.fragment_ktx)
    implementation(Libs.constraint_layout)
    implementation(Libs.lifecycle_extensions)
    implementation(Libs.lifecycle_viewmodel_ktx)

    implementation(Libs.swipe_refresh_layout)
    implementation(Libs.recyclerview)
    implementation(Libs.viewpager2)
    implementation(Libs.material)

    implementation(Libs.google_api_client) {
        exclude(group = Libs.excludePackage)
    }
    implementation(Libs.google_api_youtube) {
        exclude(group = Libs.excludePackage)
    }
    implementation(Libs.youtube_player)
    implementation(Libs.play_services)

    implementation(Libs.koin_core)
    implementation(Libs.koin_androidx_scope)
    implementation(Libs.koin_androidx_viewmodel)

    implementation(Libs.room_runtime)
    kapt(Libs.room_compiler)
    implementation(Libs.room_ktx)

    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    implementation(Libs.retrofit)
    implementation(Libs.retrofit_gson)

    implementation(Libs.conscrypt)
    implementation(Libs.threetenabp)
    implementation(Libs.easy_permission)
    implementation(Libs.coil)

    implementation(Libs.timber)

    implementation(Libs.logging_interceptor)

    //local tests
    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.mockito_unit)
    androidTestImplementation(TestLibs.hamcrest)

    //instrumented tests
    androidTestImplementation(TestLibs.mockito_android)
    androidTestImplementation(TestLibs.junit_ext)
    androidTestImplementation(TestLibs.espresso)

    //jvm testing
    testImplementation(TestLibs.junit_ext_ktx)
    testImplementation(TestLibs.androidx_test_core_ktx)
    testImplementation(TestLibs.robolectric)
    testImplementation(TestLibs.androidx_arch_core_testing)
    testImplementation(TestLibs.coroutines_test)

}
