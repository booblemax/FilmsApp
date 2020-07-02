object Apps {
    const val appId = "com.example.filmsapp"

    const val compileSdk = 29
    const val buildToolVersion = "29.0.2"
    const val minSdk = 21
    const val targetSdk = 29
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Classpath {
    const val testInstrumentalRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object Versions {
    const val gradle = "3.5.0"
    const val kotlin = "1.3.72"
    const val lifecycler = "2.2.0"
    const val appcompat = "1.1.0"
    const val ktx = "1.3.0"
    const val fragment_ktx = "1.2.5"
    const val constraintLayout = "1.1.3"
    const val swipeRefreshLayout = "1.0.0"
    const val material = "1.1.0"
    const val koin = "2.0.1"
    const val navigation = "2.2.0"
    const val retrofit = "2.8.1"
    const val coil = "0.11.0"
    const val timber = "4.7.1"
    const val loggingInterceptor = "4.4.1"
    const val viewPager = "1.0.0"
    const val room = "2.2.5"
    const val conscrypt = "2.4.0"
    const val threetenabp = "1.2.3"
    const val googleApiClient = "1.22.0"
    const val googleApiYoutube = "v3-rev183-1.22.0"
    const val playServices = "18.0.0"
    const val easyPermission = "3.0.0"
    const val youtubePlayer = "0.23"

    /* test */
    const val junit = "4.13"
    const val junit_ext = "1.1.1"
    const val espresso_core = "3.2.0"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val core_ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragment_ktx}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycler}"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycler}"
    const val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.material}"
    const val material = "com.google.android.material:material:${Versions.material}"

    const val play_services = "com.google.android.gms:play-services-auth:${Versions.playServices}"
    const val google_api_client = "com.google.api-client:google-api-client-android:${Versions.googleApiClient}"
    const val google_api_youtube = "com.google.apis:google-api-services-youtube:${Versions.googleApiYoutube}"

    const val excludePackage = "org.apache.httpcomponents"

    const val koin_core = "org.koin:koin-core:${Versions.koin}"
    const val koin_androidx_viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koin_androidx_scope = "org.koin:koin-androidx-scope:${Versions.koin}"

    const val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"

    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager}"

    const val navigation_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    const val conscrypt = "org.conscrypt:conscrypt-android:${Versions.conscrypt}"

    const val threetenabp = "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"
    const val easy_permission = "pub.devrel:easypermissions:${Versions.easyPermission}"

    const val coil = "io.coil-kt:coil:${Versions.coil}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.loggingInterceptor}"

    const val youtube_player = "com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:${Versions.youtubePlayer}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val junit_ext = "androidx.test.ext:junit:${Versions.junit_ext}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
}