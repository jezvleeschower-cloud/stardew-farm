package com.epico.stardewfarm.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"

    const val ARG_ID = "id"
    const val DETALLE = "detalle/{$ARG_ID}"
    const val FORM = "form?$ARG_ID={$ARG_ID}"

    fun detalle(id: Int) = "detalle/$id"
    fun form(id: Int? = null) = if (id == null) "form" else "form?$ARG_ID=$id"
}