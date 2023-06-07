package com.mirko.menuapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class MenuApp : Application() {

    companion object {
        lateinit var instance: MenuApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}