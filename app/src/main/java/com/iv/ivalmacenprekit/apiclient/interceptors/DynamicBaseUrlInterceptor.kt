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
        val originalUrl = request.url

        if (!originalUrl.toString().contains("LoginPrincipal")) {
            val wsUrl = sessionPreferences.wsUrl
            if (!wsUrl.isNullOrBlank()) {
                val newBaseUrl = wsUrl.toHttpUrl()

                val relativePath = originalUrl.encodedPath.substringAfterLast("json/")

                val newUrlBuilder = newBaseUrl.newBuilder()
                    .addPathSegments(relativePath)

                for (i in 0 until originalUrl.querySize) {
                    newUrlBuilder.addQueryParameter(
                        originalUrl.queryParameterName(i),
                        originalUrl.queryParameterValue(i)
                    )
                }

                val newUrl = newUrlBuilder.build()

                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
        }

        return chain.proceed(request)
    }
}