package com.iv.ivalmacenprekit.di

import com.iv.ivalmacenprekit.apiclient.AlmacenApiService
import com.iv.ivalmacenprekit.apiclient.AuthPrincipalApiService
import com.iv.ivalmacenprekit.features.auth.AuthRepository
import com.iv.ivalmacenprekit.features.auth.AuthRepositoryImpl
import com.iv.ivalmacenprekit.features.sucursales.SucursalesRepository
import com.iv.ivalmacenprekit.features.sucursales.SucursalesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authPrincipalApi: AuthPrincipalApiService,
        almacenApiService: AlmacenApiService): AuthRepository {
        return AuthRepositoryImpl(authPrincipalApi, almacenApiService)
    }

    @Provides
    @Singleton
    fun provideSucursalesRepository(
        almacenApiService: AlmacenApiService
    ): SucursalesRepository {
        return SucursalesRepositoryImpl(almacenApiService)
    }
}