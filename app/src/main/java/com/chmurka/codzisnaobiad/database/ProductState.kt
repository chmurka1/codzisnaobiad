package com.chmurka.codzisnaobiad.database

data class ProductState (
    val products: List<Product> = emptyList(),
    val currentProduct: Product = Product(),
    val name: String = "",
    val proteins: Float = 0f,
    val fat: Float = 0f,
    val carbs: Float = 0f,
    val energyKcal: Float = 0f,
    val isProductAdded: Boolean = false
)