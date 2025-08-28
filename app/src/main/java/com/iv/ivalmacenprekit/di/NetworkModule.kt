package com.iv.ivalmacenprekit.di

import android.util.Log
import com.iv.ivalmacenprekit.apiclient.AlmacenApiService
import com.iv.ivalmacenprekit.apiclient.AuthPrincipalApiService
import com.iv.ivalmacenprekit.apiclient.interceptors.DynamicBaseUrlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val BASE_LOGIN_URL =
        "https://ws.integralvending.com.mx/Movil/LoginPrincipal/Service1.svc/json/"
    private val BASE_ALMACEN_URL =
        "https://ws.integralvending.com.mx/Movil/Ejemplo/Almacen300/Service1.svc/json/"

    @Provides
    @Singleton
    fun provideOkHttpClient(
        dynamicBaseUrlInterceptor: DynamicBaseUrlInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("ApiClientLog", message)
        }.apply { level = HttpLoggingInterceptor.Level.HEADERS }

        val bodyLoggingInterceptor = Interceptor { chain ->
            val request = chain.request()
            if (request.method == "POST" || request.method == "PUT") {
                val buffer = Buffer()
                request.body?.writeTo(buffer)
                Log.d(
                    "ApiClientLog",
                    "Request ${request.method} to ${request.url}\nBody: ${buffer.readUtf8()}"
                )
            } else {
                Log.d("ApiClientLog", "Request ${request.method} to ${request.url}")
            }
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(bodyLoggingInterceptor)
            .addInterceptor(logging)
            .addInterceptor(dynamicBaseUrlInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("LoginPrincipalRetrofit")
    fun provideLoginRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_LOGIN_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("AlmacenRetrofit")
    fun provideAlmacenRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_ALMACEN_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLoginApi(@Named("LoginPrincipalRetrofit") retrofit: Retrofit): AuthPrincipalApiService =
        retrofit.create(AuthPrincipalApiService::class.java)

    @Provides
    @Singleton
    fun provideAlmacenApi(@Named("AlmacenRetrofit") retrofit: Retrofit): AlmacenApiService =
        retrofit.create(AlmacenApiService::class.java)
}