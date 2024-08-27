package work.racka.reluct.data.model.domain.billing

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