package com.apimorlabs.reluct.data.model.domain.auth

data class User(
    val id: String,
    val displayName: String,
    val profilePicUrl: String?,
    val email: String,
    val isEmailVerified: Boolean
)
