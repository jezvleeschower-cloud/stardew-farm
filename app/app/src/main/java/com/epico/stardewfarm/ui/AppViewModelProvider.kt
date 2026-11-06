package com.epico.stardewfarm.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.epico.stardewfarm.StardewApp
import com.epico.stardewfarm.ui.auth.LoginViewModel
import com.epico.stardewfarm.ui.auth.RegisterViewModel
import com.epico.stardewfarm.ui.cultivos.detalle.DetalleViewModel
import com.epico.stardewfarm.ui.cultivos.form.FormViewModel
import com.epico.stardewfarm.ui.cultivos.lista.CultivosViewModel

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(stardewApp().container.authRepository)
        }
        initializer {
            RegisterViewModel(stardewApp().container.authRepository)
        }
        initializer {
            CultivosViewModel(
                cultivoRepository = stardewApp().container.cultivoRepository,
                authRepository = stardewApp().container.authRepository,
            )
        }
        initializer {
            DetalleViewModel(
                savedStateHandle = createSavedStateHandle(),
                cultivoRepository = stardewApp().container.cultivoRepository,
            )
        }
        initializer {
            FormViewModel(
                savedStateHandle = createSavedStateHandle(),
                cultivoRepository = stardewApp().container.cultivoRepository,
            )
        }
    }
}

private fun CreationExtras.stardewApp(): StardewApp =
    this[APPLICATION_KEY] as StardewApp