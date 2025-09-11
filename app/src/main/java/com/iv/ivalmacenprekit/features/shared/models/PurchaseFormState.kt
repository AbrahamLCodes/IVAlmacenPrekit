package com.iv.ivalmacenprekit.features.shared.models

import com.iv.ivalmacenprekit.apiclient.dto.DataItemDto
import java.util.Calendar

data class PurchaseFormState(
    val noOrden: String = "",
    val noCompra: String = "",
    val selectedDateTime: String = "Selecciona fecha y hora",
    val selectedCalendar: Calendar = Calendar.getInstance(),
    val selectedAlmacen: DataItemDto? = null,
    val selectedProveedor: DataItemDto? = null,
    val selectedTipoCompra: DataItemDto? = null,
    val observaciones: String = "",
    val datePickerMillis: Long? = null
)


