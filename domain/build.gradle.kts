plugins {
    id("kotlin")
    id("idea")
    kotlin("kapt")
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.coroutines)

    implementation(Libs.koin_core)

    testImplementation(TestLibs.junit)
    testImplementation(TestLibs.mockito_unit)
    testImplementation(TestLibs.hamcrest)
}
