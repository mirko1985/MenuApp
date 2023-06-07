package com.mirko.menuapp.data.login

import com.mirko.menuapp.data.MenuAppApiResponse

data class LoginResponse (
    override var data : Data? = null
) : MenuAppApiResponse()