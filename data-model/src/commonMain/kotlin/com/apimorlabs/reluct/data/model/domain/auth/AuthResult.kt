package com.apimorlabs.reluct.data.model.domain.auth

enum class EmailResult {
    VALID, BLANK, INVALID
}

enum class PasswordResult {
    VALID, DIGITS_LETTERS_ABSENT, INCORRECT_LENGTH
}
