package com.university.bloom.data.userpreferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.userDataStore by preferencesDataStore(name = "userPreferences")

class UserPreferencesRepository @Inject constructor(@ApplicationContext appContext: Context) {
    private val userDataStore = appContext.userDataStore

    private object Keys {
        val USERNAME = stringPreferencesKey("username")
    }

    suspend fun setUsername(username: String) {
        userDataStore.edit { preferences ->
            preferences[Keys.USERNAME] = username
        }
    }

    val username: Flow<String> = userDataStore.data.map { preferences ->
        preferences[Keys.USERNAME] ?: ""
    }
}