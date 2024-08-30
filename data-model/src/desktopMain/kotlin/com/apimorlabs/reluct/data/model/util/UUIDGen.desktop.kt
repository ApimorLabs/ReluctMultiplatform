package com.apimorlabs.reluct.data.model.util

import java.util.UUID

actual object UUIDGen {
    actual fun getString(): String = UUID.randomUUID().toString()
}
