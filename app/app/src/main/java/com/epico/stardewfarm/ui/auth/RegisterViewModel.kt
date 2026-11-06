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

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmacion: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmacion: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registroExitoso: Boolean = false,
)

class RegisterViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNombreChange(valor: String) = _uiState.update {
        it.copy(nombre = valor, errorNombre = null, errorMessage = null)
    }

    fun onCorreoChange(valor: String) = _uiState.update {
        it.copy(correo = valor, errorCorreo = null, errorMessage = null)
    }

    fun onContrasenaChange(valor: String) = _uiState.update {
        it.copy(contrasena = valor, errorContrasena = null, errorMessage = null)
    }

    fun onConfirmacionChange(valor: String) = _uiState.update {
        it.copy(confirmacion = valor, errorConfirmacion = null, errorMessage = null)
    }

    fun onRegistrarClick() {
        val actual = _uiState.value
        if (actual.isLoading) return

        val errorNombre = Validators.nombre(actual.nombre)
        val errorCorreo = Validators.correo(actual.correo)
        val errorContrasena = Validators.contrasena(actual.contrasena)
        val errorConfirmacion = Validators.confirmacion(actual.contrasena, actual.confirmacion)

        if (listOf(errorNombre, errorCorreo, errorContrasena, errorConfirmacion).any { it != null }) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorCorreo = errorCorreo,
                    errorContrasena = errorContrasena,
                    errorConfirmacion = errorConfirmacion,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val resultado = authRepository.registrar(
                nombre = actual.nombre.trim(),
                correo = actual.correo.trim(),
                contrasena = actual.contrasena,
            )
            _uiState.update {
                when (resultado) {
                    is AppResult.Success -> it.copy(isLoading = false, registroExitoso = true)
                    is AppResult.Failure -> it.copy(
                        isLoading = false,
                        errorMessage = resultado.error.toMessage(
                            http400 = "Ese correo ya está registrado"
                        ),
                    )
                }
            }
        }
    }
}