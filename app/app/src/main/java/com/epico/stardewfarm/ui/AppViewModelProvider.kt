package com.epico.stardewfarm.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.epico.stardewfarm.StardewApp
import com.epico.stardewfarm.ui.auth.LoginViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(stardewApp().container.authRepository)
        }
    }
}

private fun CreationExtras.stardewApp(): StardewApp =
    this[APPLICATION_KEY] as StardewApp