package com.mirko.menuapp.data.directory

data class CurrencySettings(
    val currency_space: Boolean,
    val decimal_separator: String,
    val symbol_position: String,
    val thousands_separator: String
)