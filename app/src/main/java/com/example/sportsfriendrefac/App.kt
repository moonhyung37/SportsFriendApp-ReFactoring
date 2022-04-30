package com.example.sportsfriendrefac

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import timber.log.Timber


@HiltAndroidApp
class App : Application() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MyDataStore")

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

    //객체 생성
    init {
        instance = this
    }

    fun context(): Context {
        return instance.applicationContext
    }


    //Preference DataStore 읽기/쓰기 메서드 모음

    suspend fun saveInt(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context().dataStore.edit { preference ->
            preference[dataStoreKey] = value
        }
    }

    suspend fun readInt(key: String): Int? {
        val dataStoreKey = intPreferencesKey(key)
        val preferences = context().dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun saveString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context().dataStore.edit { preference ->
            preference[dataStoreKey] = value
        }
    }

    suspend fun readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context().dataStore.data.first()
        return preferences[dataStoreKey]
    }
}