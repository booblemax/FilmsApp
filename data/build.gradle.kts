plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Apps.compileSdk)

    defaultConfig {
        minSdkVersion(Apps.minSdk)
        testInstrumentationRunner = Classpath.testInstrumentalRunner
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            buildConfigField("String", "TOKEN", project.property("token") as String)
            buildConfigField("String", "GOOGLE_API_KEY", project.property("googleApiKey") as String)
        }
        getByName("release") {
            buildConfigField("String", "TOKEN", project.property("token") as String)
            buildConfigField("String", "GOOGLE_API_KEY", project.property("googleApiKey") as String)
            isMinifyEnabled = true
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(Libs.kotlin)
    implementation(Libs.coroutines)
    implementation(Libs.core_ktx)

    implementation(Libs.google_api_client) {
        exclude(group = Libs.excludePackage)
    }
    implementation(Libs.google_api_youtube) {
        exclude(group = Libs.excludePackage)
    }

    implementation(Libs.koin_core)
    implementation(Libs.koin_androidx_scope)

    implementation(Libs.room_runtime)
    kapt(Libs.room_compiler)
    implementation(Libs.room_ktx)

    implementation(Libs.retrofit)
    implementation(Libs.retrofit_gson)

    implementation(Libs.timber)
    implementation(Libs.logging_interceptor)
    implementation(Libs.threetenabp)

    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.mockito_unit)
    androidTestImplementation(TestLibs.hamcrest)

    androidTestImplementation(TestLibs.junit_ext)
    androidTestImplementation(TestLibs.espresso)

    testImplementation(TestLibs.junit_ext_ktx)
    testImplementation(TestLibs.androidx_test_core_ktx)
    testImplementation(TestLibs.coroutines_test)
    testImplementation(TestLibs.room_testing)
}