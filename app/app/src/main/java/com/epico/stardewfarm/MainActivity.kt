package com.epico.stardewfarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.epico.stardewfarm.ui.navigation.AppNavHost
import com.epico.stardewfarm.ui.navigation.Routes
import com.epico.stardewfarm.ui.theme.StardewFarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = (application as StardewApp).container
        val inicio = if (container.authRepository.haySesion()) Routes.HOME else Routes.LOGIN

        setContent {
            StardewFarmTheme {
                AppNavHost(startDestination = inicio)
            }
        }
    }
}