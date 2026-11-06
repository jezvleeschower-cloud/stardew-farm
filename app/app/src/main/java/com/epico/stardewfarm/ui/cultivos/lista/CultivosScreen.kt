package com.epico.stardewfarm.ui.cultivos.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun CultivosRoute(
    mensajeEntrante: String?,
    onMensajeEntranteConsumido: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    onSesionCerrada: () -> Unit,
    viewModel: CultivosViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Recarga al entrar y al volver de detalle o formulario.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.cargar() }

    LaunchedEffect(state.sesionCerrada) {
        if (state.sesionCerrada) onSesionCerrada()
    }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }

    LaunchedEffect(mensajeEntrante) {
        mensajeEntrante?.let {
            onMensajeEntranteConsumido()
            snackbarHostState.showSnackbar(it)
        }
    }

    CultivosScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onReintentar = viewModel::cargar,
        onAbrirDetalle = onAbrirDetalle,
        onNuevo = onNuevo,
        onEditar = onEditar,
        onEliminarClick = viewModel::onEliminarClick,
        onConfirmarEliminar = viewModel::onConfirmarEliminar,
        onCancelarEliminar = viewModel::onCancelarEliminar,
        onCerrarSesion = viewModel::onCerrarSesion,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CultivosScreen(
    state: CultivosUiState,
    snackbarHostState: SnackbarHostState,
    onReintentar: () -> Unit,
    onAbrirDetalle: (Int) -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    onEliminarClick: (Cultivo) -> Unit,
    onConfirmarEliminar: () -> Unit,
    onCancelarEliminar: () -> Unit,
    onCerrarSesion: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis cultivos") },
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevo) {
                Icon(Icons.Default.Add, contentDescription = "Agregar cultivo")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.isLoading && state.cultivos.isEmpty() ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.errorMessage != null && state.cultivos.isEmpty() ->
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

                state.cultivos.isEmpty() ->
                    Text(
                        text = "Aún no tienes cultivos.\nToca + para agregar el primero.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        textAlign = TextAlign.Center,
                    )

                else ->
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.cultivos, key = { it.id }) { cultivo ->
                            CultivoItem(
                                cultivo = cultivo,
                                onClick = { onAbrirDetalle(cultivo.id) },
                                onEditar = { onEditar(cultivo.id) },
                                onEliminar = { onEliminarClick(cultivo) },
                            )
                        }
                    }
            }
        }
    }

    state.cultivoAEliminar?.let { cultivo ->
        AlertDialog(
            onDismissRequest = onCancelarEliminar,
            title = { Text("Eliminar cultivo") },
            text = { Text("¿Seguro que quieres eliminar «${cultivo.nombre}»? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = onConfirmarEliminar) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = onCancelarEliminar) { Text("Cancelar") }
            },
        )
    }
}

@Composable
private fun CultivoItem(
    cultivo: Cultivo,
    onClick: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cultivo.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${cultivo.estacion.etiqueta} · ${cultivo.precioVenta} g · ${cultivo.diasCrecimiento} días",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}