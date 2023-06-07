package com.mirko.menuapp.data.login

data class CustomerAccount(
    val brand_id: Int? = null,
    val confirmed: Boolean? = null,
    val created_at: String? = null,
    val customer_id: Int? = null,
    val demographics: List<Any>? = null,
    val email: String? = null,
    val first_name: String? = null,
    val has_pending_email_change: Boolean? = null,
    val has_pending_phone_number_change: Boolean? = null,
    val id: Int? = null,
    val is_social: Boolean? = null,
    val last_name: String? = null,
    val locale: String? = null,
    val optin_status_email: Int? = null,
    val optin_status_pn: Int? = null,
    val phone_number: String? = null,
    val reference_type: String? = null,
    val social_login: Any? = null,
    val state: Int? = null,
    val type_id: Int? = null,
    val updated_at: String? = null
)