package com.mirko.menuapp.data.login

import com.mirko.menuapp.data.MenuAppApiResponse

data class LoginErrorResponse(
    override val data: LoginError? = null
) : MenuAppApiResponse()

data class LoginError(
    val validations: Validations,
    val info_message: InfoMessage,
    val message: String
) 