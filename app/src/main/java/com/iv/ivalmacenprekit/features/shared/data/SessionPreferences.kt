package com.iv.ivalmacenprekit.features.shared.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    var wsUrl: String?
        get() = prefs.getString("ws_url", null)
        set(value) = prefs.edit().putString("ws_url", value).apply()

    var idUsuario: Int
        get() = prefs.getInt("id_usuario", -1)
        set(value) = prefs.edit().putInt("id_usuario", value).apply()

    var idAlmacen: Int
        get() = prefs.getInt("id_almacen", -1)
        set(value) = prefs.edit().putInt("id_almacen", value).apply()

    var idSucursal: Int
        get() = prefs.getInt("id_sucursal", -1)
        set(value) = prefs.edit().putInt("id_sucursal", value).apply()

    var nombreSucursal: String?
        get() = prefs.getString("nombre_sucursal", null)
        set(value) = prefs.edit().putString("nombre_sucursal", value).apply()
}