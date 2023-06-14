package com.chmurka.codzisnaobiad.database

sealed interface FridgeProductEvent {
    object SaveFridgeProduct: FridgeProductEvent
    data class SetProduct( val id: Int) : FridgeProductEvent
    data class SetExpirationDate(val date: String) : FridgeProductEvent
    data class SetAmount( val value: Float) : FridgeProductEvent
    data class DeleteFridgeProduct(val product: FridgeProduct) : FridgeProductEvent
    object ShowDialog: FridgeProductEvent
    object HideDialog: FridgeProductEvent
    data class SetCurrentFridgeProduct(val fridgeProduct: FridgeProduct) : FridgeProductEvent
}