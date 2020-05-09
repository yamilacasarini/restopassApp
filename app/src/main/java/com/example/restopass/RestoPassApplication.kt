package com.example.restopass

import android.app.Application
import timber.log.Timber

// Application Class: contiene el estado global de toda la aplicación.
// Sirve para setear Timber (librería de logs) globalmente
class RestoPassApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}