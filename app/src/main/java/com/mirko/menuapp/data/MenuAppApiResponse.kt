package com.mirko.menuapp.data

open class MenuAppApiResponse(
    val status: String? = null,
    val code: Int? = null,
    @Transient open val data: Any? = null
)