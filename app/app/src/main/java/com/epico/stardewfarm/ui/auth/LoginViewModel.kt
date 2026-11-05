package com.epico.stardewfarm.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epico.stardewfarm.domain.model.AppResult
import com.epico.stardewfarm.domain.repository.AuthRepository
import com.epico.stardewfarm.ui.common.Validators
import com.epico.stardewfarm.ui.common.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginExitoso: Boolean = false,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoChange(valor: String) = _uiState.update {
        it.copy(correo = valor, errorCorreo = null, errorMessage = null)
    }

    fun onContrasenaChange(valor: String) = _uiState.update {
        it.copy(contrasena = valor, errorContrasena = null, errorMessage = null)
    }

    fun onLoginClick() {
        val actual = _uiState.value
        if (actual.isLoading) return

        val errorCorreo = Validators.correo(actual.correo)
        val errorContrasena =
            if (actual.contrasena.isEmpty()) "La contraseña es obligatoria" else null

        if (errorCorreo != null || errorContrasena != null) {
            _uiState.update {
                it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val resultado = authRepository.iniciarSesion(actual.correo.trim(), actual.contrasena)
            _uiState.update {
                when (resultado) {
                    is AppResult.Success -> it.copy(isLoading = false, loginExitoso = true)
                    is AppResult.Failure -> it.copy(
                        isLoading = false,
                        errorMessage = resultado.error.toMessage(
                            http400 = "Correo o contraseña incorrectos"
                        ),
                    )
                }
            }
        }
    }
}