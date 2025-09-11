package com.iv.ivalmacenprekit.features.shared.models

import android.net.Uri
import com.iv.ivalmacenprekit.apiclient.dto.ArticuloCompraDto
import com.iv.ivalmacenprekit.features.shared.customtoast.ToastType

data class PurchaseDataState(
    val showSheet: Boolean = false,
    val selectedArticle: ArticuloCompraDto? = null,
    val showDetailSheet: Boolean = false,
    val showEvidenceSheet: Boolean = false,
    val invoiceNumber: String = "",
    val photoUri: Uri? = null,
    val toastVisible: Boolean = false,
    val toastMessage: String = "",
    val toastType: ToastType = ToastType.INFO,
    val showResumeSheet: Boolean = false,
    val addedData: List<ArticuloCompraDto> = emptyList(),
)
