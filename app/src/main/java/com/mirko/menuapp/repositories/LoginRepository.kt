package com.mirko.menuapp

import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mirko.menuapp.data.login.LoginErrorResponse
import com.mirko.menuapp.data.login.LoginRequest
import com.mirko.menuapp.data.login.LoginResponse
import com.mirko.menuapp.repositories.retrofit.NetworkResponse
import com.mirko.menuapp.repositories.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

object LoginRepository {

    private val tokenPreferencesKey = stringPreferencesKey("token")

    suspend fun login(
        email: String?,
        password: String?
    ): Flow<NetworkResponse<LoginResponse, LoginErrorResponse>> {
        return flow {
            emit(RetrofitHelper.loginService.login(LoginRequest(email!!, password!!)))
        }.flowOn(Dispatchers.IO)
            .onEach { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        storeToken(response.body.data?.token?.value)
                    }
                    else -> {}
                }
            }
            .catch { error ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(MenuApp.instance, error.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    internal suspend fun storeToken(token: String?) {
        MenuApp.instance.dataStore.edit { preferences ->
            preferences[tokenPreferencesKey] = token ?: ""
        }
    }

    fun getToken(): Flow<String?> {
        return MenuApp.instance.dataStore.data.map { preferences ->
            preferences[tokenPreferencesKey]
        }
    }
}
