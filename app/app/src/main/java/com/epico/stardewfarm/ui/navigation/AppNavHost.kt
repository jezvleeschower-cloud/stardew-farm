package com.epico.stardewfarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epico.stardewfarm.ui.auth.LoginRoute
import com.epico.stardewfarm.ui.auth.RegisterRoute
import com.epico.stardewfarm.ui.cultivos.detalle.DetalleRoute
import com.epico.stardewfarm.ui.cultivos.form.FormRoute
import com.epico.stardewfarm.ui.cultivos.lista.CultivosRoute

private const val KEY_MENSAJE = "mensaje"

@Composable
fun AppNavHost(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginRoute(
                onLoginExitoso = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Routes.REGISTER) },
            )
        }

        composable(Routes.REGISTER) {
            RegisterRoute(
                onRegistroExitoso = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onIrALogin = { navController.popBackStack() },
            )
        }

        composable(Routes.HOME) { entry ->
            val mensaje by entry.savedStateHandle
                .getStateFlow<String?>(KEY_MENSAJE, null)
                .collectAsStateWithLifecycle()

            CultivosRoute(
                mensajeEntrante = mensaje,
                onMensajeEntranteConsumido = { entry.savedStateHandle[KEY_MENSAJE] = null },
                onAbrirDetalle = { id -> navController.navigate(Routes.detalle(id)) },
                onNuevo = { navController.navigate(Routes.form()) },
                onEditar = { id -> navController.navigate(Routes.form(id)) },
                onSesionCerrada = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Routes.DETALLE,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.IntType }),
        ) {
            DetalleRoute(
                onVolver = { navController.popBackStack() },
                onEditar = { id -> navController.navigate(Routes.form(id)) },
            )
        }

        composable(
            route = Routes.FORM,
            arguments = listOf(
                navArgument(Routes.ARG_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
        ) {
            FormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { mensaje ->
                    navController.getBackStackEntry(Routes.HOME)
                        .savedStateHandle[KEY_MENSAJE] = mensaje
                    navController.popBackStack(Routes.HOME, inclusive = false)
                },
            )
        }
    }
}