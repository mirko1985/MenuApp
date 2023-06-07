package com.mirko.menuapp.data.login

data class Token(
    val issued_token_type: String? = null,
    val refresh_ttl: Int? = null,
    val token_type: String? = null,
    val ttl: Int? = null,
    val value: String? = null
)