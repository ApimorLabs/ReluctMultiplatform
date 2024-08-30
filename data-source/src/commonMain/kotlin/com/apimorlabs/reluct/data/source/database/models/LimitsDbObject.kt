package com.apimorlabs.reluct.data.source.database.models

data class LimitsDbObject(
    val packageName: String,
    val timeLimit: Long,
    val isADistractingAp: Boolean,
    val isPaused: Boolean,
    val overridden: Boolean
)
