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
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
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
    implementation(project(":data"))
    implementation(project(":domain"))

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

    implementation(Libs.youtube_player)
    implementation(Libs.play_services)

    implementation(Libs.koin_core)
    implementation(Libs.koin_androidx_scope)
    implementation(Libs.koin_androidx_viewmodel)

    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    implementation(Libs.conscrypt)
    implementation(Libs.threetenabp)
    implementation(Libs.easy_permission)
    implementation(Libs.coil)

    implementation(Libs.timber)

    // local tests
    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.mockito_unit)
    androidTestImplementation(TestLibs.hamcrest)

    // instrumented tests
    androidTestImplementation(TestLibs.mockito_android)
    androidTestImplementation(TestLibs.junit_ext)
    androidTestImplementation(TestLibs.espresso)

    // jvm testing
    testImplementation(TestLibs.junit_ext_ktx)
    testImplementation(TestLibs.androidx_test_core_ktx)
    testImplementation(TestLibs.robolectric) {
        exclude(group = TestLibs.guava_group, module = TestLibs.guava_jdk5_module)
    }
    testImplementation(TestLibs.androidx_arch_core_testing)
    testImplementation(TestLibs.coroutines_test)

    testImplementation(TestLibs.guava_android)
}
