package com.iv.ivalmacenprekit.apiclient.interceptors

import android.util.Log
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DynamicBaseUrlInterceptor @Inject constructor(
    private val sessionPreferences: SessionPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalUrl = request.url.toString()

        // Only modify requests that are NOT LoginPrincipal
        if (!originalUrl.contains("LoginPrincipal")) {
            val wsUrl = sessionPreferences.wsUrl
            if (!wsUrl.isNullOrBlank()) {
                val newBaseUrl = wsUrl.toHttpUrl()

                // Rebuild the URL by taking the encodedPath from originalUrl
                val relativePath = request.url.encodedPath.substringAfterLast("json/")
                val newUrl = newBaseUrl.newBuilder()
                    .addPathSegments(relativePath)
                    .build()

                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
        }

        return chain.proceed(request)
    }
}