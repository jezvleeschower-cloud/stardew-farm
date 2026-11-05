package com.epico.stardewfarm.data.local

import android.content.Context

class SharedPrefsSessionStorage(context: Context) : SessionStorage {

    private val prefs = context.applicationContext
        .getSharedPreferences("stardew_session", Context.MODE_PRIVATE)

    override fun guardarToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun obtenerToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun limpiar() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    private companion object {
        const val KEY_TOKEN = "access_token"
    }
}