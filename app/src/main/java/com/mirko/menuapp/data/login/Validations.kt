package com.mirko.menuapp.data.login

data class Validations(
    val email: List<Rule>? = null,
    val password: List<Rule>? = null
)