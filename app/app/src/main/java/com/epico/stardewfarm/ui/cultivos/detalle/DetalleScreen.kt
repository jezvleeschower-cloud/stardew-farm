package com.epico.stardewfarm.ui.cultivos.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.epico.stardewfarm.domain.model.Cultivo
import com.epico.stardewfarm.ui.AppViewModelProvider

@Composable
fun DetalleRoute(
    onVolver: () -> Unit,
    onEditar: (Int) -> Unit,
    viewModel: DetalleViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.cargar() }

    DetalleScreen(
        state = state,
        onVolver = onVolver,
        onEditar = { onEditar(viewModel.cultivoId) },
        onReintentar = viewModel::cargar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    state: DetalleUiState,
    onVolver: () -> Unit,
    onEditar: () -> Unit,
    onReintentar: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del cultivo") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (state.cultivo != null) {
                        IconButton(onClick = onEditar) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
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
                state.cultivo != null -> DetalleContenido(state.cultivo)

                state.isLoading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.errorMessage != null ->
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                        Button(onClick = onReintentar) { Text("Reintentar") }
                    }
            }
        }
    }
}

@Composable
private fun DetalleContenido(cultivo: Cultivo) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(cultivo.nombre, style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider()
        Dato("Estación", cultivo.estacion.etiqueta)
        Dato("Precio de venta", "${cultivo.precioVenta} g")
        Dato("Días de crecimiento", "${cultivo.diasCrecimiento}")
        Dato("Se recultiva", if (cultivo.seRecultiva) "Sí" else "No")
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Column {
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}