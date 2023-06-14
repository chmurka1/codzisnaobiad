package com.chmurka.codzisnaobiad.database

sealed interface ProductEvent {
    object SaveProduct: ProductEvent
    data class SetName( val name: String) : ProductEvent
    data class SetProteins( val value: Float) : ProductEvent
    data class SetFat( val value: Float) : ProductEvent
    data class SetCarbs( val value: Float) : ProductEvent
    data class SetKcal( val value: Int) : ProductEvent
    data class DeleteProduct(val product: Product) : ProductEvent
    object ShowDialog: ProductEvent
    object HideDialog: ProductEvent
    data class SetCurrentProduct(val product: Product) : ProductEvent
}