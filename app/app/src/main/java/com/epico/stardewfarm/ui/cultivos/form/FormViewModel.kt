package com.epico.stardewfarm.ui.cultivos.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epico.stardewfarm.model.AppResult
import com.epico.stardewfarm.model.Cultivo
import com.epico.stardewfarm.model.Estacion
import com.epico.stardewfarm.repository.CultivoRepository
import com.epico.stardewfarm.ui.common.Validators
import com.epico.stardewfarm.ui.common.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormUiState(
    val esEdicion: Boolean = false,
    val isLoading: Boolean = false,
    val cargaFallida: Boolean = false,
    val nombre: String = "",
    val estacion: Estacion = Estacion.PRIMAVERA,
    val precioVenta: String = "",
    val diasCrecimiento: String = "",
    val seRecultiva: Boolean = false,
    val errorNombre: String? = null,
    val errorPrecio: String? = null,
    val errorDias: String? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val guardado: Boolean = false,
)

class FormViewModel(
    savedStateHandle: SavedStateHandle,
    private val cultivoRepository: CultivoRepository,
) : ViewModel() {

    // -1 (valor por defecto de la ruta) significa "cultivo nuevo".
    private val cultivoId: Int? = savedStateHandle.get<Int>("id")?.takeIf { it > 0 }

    private val _uiState = MutableStateFlow(FormUiState(esEdicion = cultivoId != null))
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    init {
        if (cultivoId != null) cargar()
    }

    fun cargar() {
        val id = cultivoId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, cargaFallida = false, errorMessage = null) }
            when (val resultado = cultivoRepository.obtener(id)) {
                is AppResult.Success -> {
                    val c = resultado.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nombre = c.nombre,
                            estacion = c.estacion,
                            precioVenta = c.precioVenta.toString(),
                            diasCrecimiento = c.diasCrecimiento.toString(),
                            seRecultiva = c.seRecultiva,
                        )
                    }
                }
                is AppResult.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        cargaFallida = true,
                        errorMessage = resultado.error.toMessage(),
                    )
                }
            }
        }
    }

    fun onNombreChange(valor: String) =
        _uiState.update { it.copy(nombre = valor, errorNombre = null) }

    fun onEstacionChange(valor: Estacion) =
        _uiState.update { it.copy(estacion = valor) }

    fun onPrecioChange(valor: String) = _uiState.update {
        it.copy(precioVenta = valor.filter(Char::isDigit).take(9), errorPrecio = null)
    }

    fun onDiasChange(valor: String) = _uiState.update {
        it.copy(diasCrecimiento = valor.filter(Char::isDigit).take(3), errorDias = null)
    }

    fun onSeRecultivaChange(valor: Boolean) =
        _uiState.update { it.copy(seRecultiva = valor) }

    fun onGuardarClick() {
        val actual = _uiState.value
        if (actual.isSaving) return

        val errorNombre = Validators.nombreCultivo(actual.nombre)
        val errorPrecio = Validators.precioVenta(actual.precioVenta)
        val errorDias = Validators.diasCrecimiento(actual.diasCrecimiento)

        if (errorNombre != null || errorPrecio != null || errorDias != null) {
            _uiState.update {
                it.copy(errorNombre = errorNombre, errorPrecio = errorPrecio, errorDias = errorDias)
            }
            return
        }

        val cultivo = Cultivo(
            id = cultivoId ?: 0,
            nombre = actual.nombre.trim(),
            estacion = actual.estacion,
            precioVenta = actual.precioVenta.toInt(),
            diasCrecimiento = actual.diasCrecimiento.toInt(),
            seRecultiva = actual.seRecultiva,
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val resultado = if (cultivoId == null) {
                cultivoRepository.crear(cultivo)
            } else {
                cultivoRepository.actualizar(cultivo)
            }
            _uiState.update {
                when (resultado) {
                    is AppResult.Success -> it.copy(isSaving = false, guardado = true)
                    is AppResult.Failure -> it.copy(
                        isSaving = false,
                        errorMessage = resultado.error.toMessage(),
                    )
                }
            }
        }
    }
}