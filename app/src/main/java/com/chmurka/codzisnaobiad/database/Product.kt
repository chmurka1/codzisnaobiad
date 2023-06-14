package com.chmurka.codzisnaobiad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String = "",
    val proteins100g: Float = 0f,
    val fat100g: Float = 0f,
    val carbohydrates100g: Float = 0f,
    val energyKcal: Float = 0f
)