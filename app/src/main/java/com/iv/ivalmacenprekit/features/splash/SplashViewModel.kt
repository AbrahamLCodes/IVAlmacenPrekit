package com.iv.ivalmacenprekit.features.splash

import androidx.lifecycle.ViewModel
import com.iv.ivalmacenprekit.features.shared.data.SessionPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionPreferences: SessionPreferences
): ViewModel() {

    val isLoggedIn: Boolean
        get() = sessionPreferences.idUsuario != -1

}