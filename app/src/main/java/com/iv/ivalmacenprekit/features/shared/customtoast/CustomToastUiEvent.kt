package com.iv.ivalmacenprekit.features.shared.customtoast

sealed class UiEvent {
    data class ShowToast(val message: String, val type: ToastType) : UiEvent()
    object LoginClienteSuccess : UiEvent()
    object LoginSaasSuccess : UiEvent()
    object SucursalSavedSuccess : UiEvent()
}