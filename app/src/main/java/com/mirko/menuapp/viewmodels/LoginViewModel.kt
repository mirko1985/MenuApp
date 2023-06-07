package com.mirko.menuapp.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirko.menuapp.LoginRepository
import com.mirko.menuapp.repositories.retrofit.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val email = ObservableField("")
    val password = ObservableField("")

    val emailError: ObservableField<String?> = ObservableField("")
    val passwordError: ObservableField<String?> = ObservableField("")

    val loginSuccess = MutableLiveData(false)

    fun checkLogin(): Flow<String?> {
        return LoginRepository.getToken()
    }

    fun login() {
        viewModelScope.launch {

            LoginRepository.login(email.get(), password.get()).collect { response ->

                emailError.set(null)
                passwordError.set(null)

                when (response) {
                    is NetworkResponse.Success -> {
                        loginSuccess.value = true
                    }
                    is NetworkResponse.ApiError -> {
                        // handle login api errors
                        if (response.body.data?.validations != null) {
                            response.body.data.validations.email?.let { ruleList ->
                                val errorMsg = StringBuilder()
                                ruleList.forEach {
                                    errorMsg.append(it.message).append("\n")
                                }
                                emailError.set(errorMsg.removeSuffix("\n").toString())
                            }
                            response.body.data.validations.password?.let { ruleList ->
                                val errorMsg = StringBuilder()
                                ruleList.forEach {
                                    errorMsg.append(it.message).append("\n")
                                }
                                passwordError.set(errorMsg.removeSuffix("\n").toString())
                            }
                        } else {
                            response.body.data?.info_message?.let {
                                emailError.set(it.body)
                                passwordError.set(it.body)
                            }
                        }
                    }
                    else -> {}
                }
            }

        }
    }

    suspend fun logOut() {
        loginSuccess.value = false
        LoginRepository.storeToken(null)
    }
}