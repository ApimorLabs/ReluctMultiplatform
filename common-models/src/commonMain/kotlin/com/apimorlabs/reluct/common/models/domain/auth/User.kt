package com.apimorlabs.reluct.common.models.domain.auth

data class User(
    val id: String,
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val isEmailVerified: Boolean
)
