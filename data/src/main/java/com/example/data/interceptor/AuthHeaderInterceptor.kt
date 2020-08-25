package com.example.data.interceptor

import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${BuildConfig.TOKEN}").build()
        return chain.proceed(request)
    }
}
