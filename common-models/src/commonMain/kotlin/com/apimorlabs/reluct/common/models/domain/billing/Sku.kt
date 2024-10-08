package com.apimorlabs.reluct.common.models.domain.billing

enum class Sku(
    val type: String
) {
    Subscription(
        type = "subs"
    ),
    InAppPurchase(
        type = "inapp"
    )
}
