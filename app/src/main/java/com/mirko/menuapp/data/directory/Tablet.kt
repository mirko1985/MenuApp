package com.mirko.menuapp.data.directory

data class Tablet(
    val area_ids: List<Int>? = null,
    val failure_reported: Boolean? = null,
    val id: Int? = null,
    val is_critical: Boolean? = null,
    val is_online: Boolean? = null,
    val kiosk_notification_type: Int? = null,
    val last_request_at: String? = null,
    val middleware_endpoint: Any? = null,
    val name: String? = null,
    val pls_name: Any? = null,
    val show_kiosk_orders: Boolean? = null,
    val state: Int? = null,
    val use_notifications: Int? = null,
    val venue_id: Int? = null
)