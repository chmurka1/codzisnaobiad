package com.chmurka.codzisnaobiad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String = "",
    val desc: String = "",
    val imageUri: String? = null,
)
