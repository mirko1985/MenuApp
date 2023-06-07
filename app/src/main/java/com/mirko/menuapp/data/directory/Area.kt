package com.mirko.menuapp.data.directory

data class Area(
    val id: Int? = null,
    val is_table_supported: Int? = null,
    val menu_id: Int? = null,
    val name: String? = null,
    val order_types: List<OrderType>? = null,
    val pos_id: Any? = null,
    val properties: Properties? = null,
    val reference_type: String? = null,
    val singular_point_id: Int? = null,
    val state: Int? = null,
    val table_pos_id: Any? = null,
    val tablet: Tablet? = null,
    val type_id: Int? = null,
    val use_tablet: Boolean? = null,
    val venue_id: Int? = null
)