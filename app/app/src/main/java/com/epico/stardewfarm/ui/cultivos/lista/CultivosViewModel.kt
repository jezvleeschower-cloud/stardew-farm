package com.epico.stardewfarm.ui.cultivos.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Cultivo
import com.epico.stardewfarm.model.DataError
import com.epico.stardewfarm.repository.AuthRepository
import com.epico.stardewfarm.repository.CultivoRepository
import com.epico.stardewfarm.ui.common.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CultivosUiState(
    val isLoading: Boolean = true,
    val cultivos: List<Cultivo> = emptyList(),
    val errorMessage: String? = null,
    val cultivoAEliminar: Cultivo? = null,
    val mensaje: String? = null,
    val sesionCerrada: Boolean = false,
)

class CultivosViewModel(
    private val cultivoRepository: CultivoRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CultivosUiState())
    val uiState: StateFlow<CultivosUiState> = _uiState.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val resultado = cultivoRepository.listar()) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, cultivos = resultado.data)
                }
                is AppResult.Failure -> manejarError(resultado.error)
            }
        }
    }

    fun onEliminarClick(cultivo: Cultivo) =
        _uiState.update { it.copy(cultivoAEliminar = cultivo) }

    fun onCancelarEliminar() =
        _uiState.update { it.copy(cultivoAEliminar = null) }

    fun onConfirmarEliminar() {
        val cultivo = _uiState.value.cultivoAEliminar ?: return
        _uiState.update { it.copy(cultivoAEliminar = null) }
        viewModelScope.launch {
            when (val resultado = cultivoRepository.eliminar(cultivo.id)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(mensaje = "«${cultivo.nombre}» eliminado") }
                    cargar()
                }
                is AppResult.Failure -> manejarError(resultado.error)
            }
        }
    }

    fun onMensajeMostrado() = _uiState.update { it.copy(mensaje = null) }

    fun onCerrarSesion() {
        authRepository.cerrarSesion()
        _uiState.update { it.copy(sesionCerrada = true) }
    }

    private fun manejarError(error: DataError) {
        if (error is DataError.Http && error.code == 401) {
            onCerrarSesion()
            return
        }
        val texto = error.toMessage()
        _uiState.update {
            if (it.cultivos.isEmpty()) it.copy(isLoading = false, errorMessage = texto)
            else it.copy(isLoading = false, mensaje = texto)
        }
    }
}