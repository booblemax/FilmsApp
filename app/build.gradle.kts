plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}
apply(from = "../ktlint.gradle.kts")

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
        }
        getByName("release") {
            buildConfigField("String", "TOKEN", project.property("token") as String)
            buildConfigField("String", "BASE_URL", project.property("baseurl") as String)
            buildConfigField("String", "FULL_IMAGE_URL", project.property("fullImageUrl") as String)
            buildConfigField("String", "REDUCED_IMAGE_URL", project.property("reducedImageUrl") as String)
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    dataBinding {
        isEnabled = true
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
    implementation(Libs.core_ktx)
    implementation(Libs.fragment_ktx)
    implementation(Libs.constraint_layout)
    implementation(Libs.lifecycle_extensions)
    implementation(Libs.lifecycle_viewmodel_ktx)

    implementation(Libs.swipe_refresh_layout)
    implementation(Libs.recyclerview)
    implementation(Libs.viewpager2)
    implementation(Libs.material)

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

    implementation(Libs.coil)

    implementation(Libs.timber)
    
    implementation(Libs.logging_interceptor)

    testImplementation(TestLibs.junit)
    androidTestImplementation(TestLibs.junit_ext)
    androidTestImplementation(TestLibs.espresso)
}