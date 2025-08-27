package com.iv.ivalmacenprekit.di

import com.iv.ivalmacenprekit.apiclient.AlmacenApiService
import com.iv.ivalmacenprekit.apiclient.AuthPrincipalApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        "https://ws.integralvending.com.mx/Movil/Secic/Almacen300/Service1.svc/json/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @Named("LoginRetrofit")
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
    fun provideLoginApi(@Named("AuthPrincipalRetrofit") retrofit: Retrofit): AuthPrincipalApiService =
        retrofit.create(AuthPrincipalApiService::class.java)

    @Provides
    @Singleton
    fun provideAlmacenApi(@Named("AlmacenRetrofit") retrofit: Retrofit): AlmacenApiService =
        retrofit.create(AlmacenApiService::class.java)
}