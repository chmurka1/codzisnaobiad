package com.chmurka.codzisnaobiad.database

data class FridgeProductState (
    val fridgeProducts: Map<FridgeProduct, Product> = emptyMap(),
    val currentFridgeProduct: FridgeProduct = FridgeProduct(),
    val product: Int? = null,
    val expirationDate: String? = null,
    val amount: Float = 0f,
    val isFridgeProductAdded: Boolean = false
)