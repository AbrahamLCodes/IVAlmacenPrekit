package com.iv.ivalmacenprekit.features.ComprasData

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType
import com.iv.ivalmacenprekit.features.shared.customtoast.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseDataViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun showToast(message: String, type: ToastType) {
        viewModelScope.launch {
            Log.d("", "showToast asdasdsd")
            _uiEvent.send(UiEvent.ShowToast(message, type))
        }
    }
}