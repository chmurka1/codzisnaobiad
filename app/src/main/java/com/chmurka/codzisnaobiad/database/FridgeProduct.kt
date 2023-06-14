package com.chmurka.codzisnaobiad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fridge_products")
data class FridgeProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val product_id: Int? = null,
    val expirationDate: String? = null,
    val amount_g: Float = 0f
)