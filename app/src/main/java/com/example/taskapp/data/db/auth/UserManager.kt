package com.example.taskapp.data.db.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taskapp.data.model.User
import kotlinx.coroutines.flow.first


class UserManager(private val context: Context) {
    private val Context.userStore: DataStore<Preferences> by preferencesDataStore("user")

    companion object{
        val USER_EMAIL_KEY = stringPreferencesKey("USER_EMAIL")
        val USER_PASSWORD_KEY = stringPreferencesKey("USER_PASSWORD")
        val USER_AUTHENTICATED_KEY = booleanPreferencesKey("USER_AUTHENTICATED")
    }

    suspend fun saveUserData(email: String, password: String){
        context.userStore.edit {
            it[USER_EMAIL_KEY] = email
            it[USER_PASSWORD_KEY] = password

        }
    }

    suspend fun getUserData() : User {
        val prefs = context.userStore.data.first()
        return  User(
            email = prefs[USER_EMAIL_KEY]?: "",
            password = prefs[USER_PASSWORD_KEY]?: "",

        )
    }
}