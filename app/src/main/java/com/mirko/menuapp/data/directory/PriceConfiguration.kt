package com.mirko.menuapp.data.directory

data class PriceConfiguration(
    val external_channels: List<Any>? = null,
    val order_types: List<PriceConfigurationOrderType>? = null
)