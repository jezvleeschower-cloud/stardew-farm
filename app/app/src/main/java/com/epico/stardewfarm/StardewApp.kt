package com.epico.stardewfarm

import android.app.Application
import com.epico.stardewfarm.di.AppContainer

class StardewApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}