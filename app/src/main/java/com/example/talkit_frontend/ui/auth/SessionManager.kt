package com.example.talkit_frontend.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")
        val USER_AVATAR_KEY = stringPreferencesKey("user_avatar_key")
        val USER_FECHA_NAC_KEY = stringPreferencesKey("user_fecha_nacimiento_key")
    }

    val userName: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME_KEY] }

    val userEmail: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    val userAvatar: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_AVATAR_KEY] }
    val userFechaNacimiento: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_FECHA_NAC_KEY] }


    suspend fun saveUserData(name: String, email: String, fechaNacimiento: String, avatar: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_FECHA_NAC_KEY] = fechaNacimiento
            preferences[USER_AVATAR_KEY] = avatar
        }
    }

    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[AUTH_TOKEN]
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[AUTH_TOKEN] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear() // Esto borra TODO lo guardado
        }
    }
}
