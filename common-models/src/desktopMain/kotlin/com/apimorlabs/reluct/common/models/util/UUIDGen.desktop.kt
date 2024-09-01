package com.apimorlabs.reluct.common.models.util

import java.util.UUID

actual object UUIDGen {
    actual fun getString(): String = UUID.randomUUID().toString()
}
