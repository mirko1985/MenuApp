package com.mirko.menuapp.data.directory

data class Country(
    val available_payment_methods: List<AvailablePaymentMethod>,
    val calling_code: String,
    val code: String,
    val code_alpha3: String,
    val code_numeric: String,
    val currency_id: Int,
    val currency_settings: CurrencySettings,
    val distance_unit: String,
    val id: Int,
    val is_address_number_first: Boolean,
    val name: String,
    val supported_travel_modes: List<String>
)