package com.epico.stardewfarm.ui.cultivos.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epico.stardewfarm.model.Estacion
import com.epico.stardewfarm.ui.AppViewModelProvider

@Composable
fun FormRoute(
    onVolver: () -> Unit,
    onGuardado: (String) -> Unit,
    viewModel: FormViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardado) {
        if (state.guardado) {
            onGuardado(if (state.esEdicion) "Cultivo actualizado" else "Cultivo creado")
        }
    }

    FormScreen(
        state = state,
        onVolver = onVolver,
        onNombreChange = viewModel::onNombreChange,
        onEstacionChange = viewModel::onEstacionChange,
        onPrecioChange = viewModel::onPrecioChange,
        onDiasChange = viewModel::onDiasChange,
        onSeRecultivaChange = viewModel::onSeRecultivaChange,
        onGuardarClick = viewModel::onGuardarClick,
        onReintentar = viewModel::cargar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    state: FormUiState,
    onVolver: () -> Unit,
    onNombreChange: (String) -> Unit,
    onEstacionChange: (Estacion) -> Unit,
    onPrecioChange: (String) -> Unit,
    onDiasChange: (String) -> Unit,
    onSeRecultivaChange: (Boolean) -> Unit,
    onGuardarClick: () -> Unit,
    onReintentar: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.esEdicion) "Editar cultivo" else "Nuevo cultivo") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.isLoading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.cargaFallida ->
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = state.errorMessage.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                        Button(onClick = onReintentar) { Text("Reintentar") }
                    }

                else -> FormContenido(
                    state = state,
                    onNombreChange = onNombreChange,
                    onEstacionChange = onEstacionChange,
                    onPrecioChange = onPrecioChange,
                    onDiasChange = onDiasChange,
                    onSeRecultivaChange = onSeRecultivaChange,
                    onGuardarClick = onGuardarClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormContenido(
    state: FormUiState,
    onNombreChange: (String) -> Unit,
    onEstacionChange: (Estacion) -> Unit,
    onPrecioChange: (String) -> Unit,
    onDiasChange: (String) -> Unit,
    onSeRecultivaChange: (Boolean) -> Unit,
    onGuardarClick: () -> Unit,
) {
    var menuAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            singleLine = true,
            isError = state.errorNombre != null,
            supportingText = if (state.errorNombre != null) {
                { Text(state.errorNombre) }
            } else null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )

        ExposedDropdownMenuBox(
            expanded = menuAbierto,
            onExpandedChange = { menuAbierto = it },
        ) {
            OutlinedTextField(
                value = state.estacion.etiqueta,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false },
            ) {
                Estacion.entries.forEach { estacion ->
                    DropdownMenuItem(
                        text = { Text(estacion.etiqueta) },
                        onClick = {
                            onEstacionChange(estacion)
                            menuAbierto = false
                        },
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.precioVenta,
            onValueChange = onPrecioChange,
            label = { Text("Precio de venta (g)") },
            singleLine = true,
            isError = state.errorPrecio != null,
            supportingText = if (state.errorPrecio != null) {
                { Text(state.errorPrecio) }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.diasCrecimiento,
            onValueChange = onDiasChange,
            label = { Text("Días de crecimiento (1 a 28)") },
            singleLine = true,
            isError = state.errorDias != null,
            supportingText = if (state.errorDias != null) {
                { Text(state.errorDias) }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Se recultiva")
            Switch(checked = state.seRecultiva, onCheckedChange = onSeRecultivaChange)
        }

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Button(
            onClick = onGuardarClick,
            enabled = !state.isSaving,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Guardar")
            }
        }
    }
}