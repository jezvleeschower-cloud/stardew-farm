package com.epico.stardewfarm.ui.cultivos.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Cultivo
import com.epico.stardewfarm.repository.CultivoRepository
import com.epico.stardewfarm.ui.common.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUiState(
    val isLoading: Boolean = true,
    val cultivo: Cultivo? = null,
    val errorMessage: String? = null,
)

class DetalleViewModel(
    savedStateHandle: SavedStateHandle,
    private val cultivoRepository: CultivoRepository,
) : ViewModel() {

    private val id: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    val cultivoId: Int get() = id

    fun cargar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val resultado = cultivoRepository.obtener(id)) {
                is AppResult.Success -> _uiState.update {
                    it.copy(isLoading = false, cultivo = resultado.data)
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = resultado.error.toMessage())
                }
            }
        }
    }
}