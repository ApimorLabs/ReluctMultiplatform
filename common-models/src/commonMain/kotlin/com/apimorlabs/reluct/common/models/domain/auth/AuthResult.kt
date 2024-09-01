package com.apimorlabs.reluct.common.models.domain.auth

enum class EmailResult {
    VALID, BLANK, INVALID
}

enum class PasswordResult {
    VALID, DIGITS_LETTERS_ABSENT, INCORRECT_LENGTH
}
