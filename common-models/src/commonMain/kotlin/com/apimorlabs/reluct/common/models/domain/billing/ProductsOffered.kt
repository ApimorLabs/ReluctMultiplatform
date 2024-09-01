package com.apimorlabs.reluct.common.models.domain.billing

enum class ProductsOffered(
    val skuType: Sku,
    val id: String
) {
    Premium(
        skuType = Sku.Subscription,
        id = "premium_subscription"
    ),
}
