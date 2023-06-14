package com.chmurka.codzisnaobiad.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Upsert
    suspend fun insertRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Upsert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Upsert
    suspend fun insertFridgeProduct(fridgeProduct: FridgeProduct)

    @Delete
    suspend fun deleteFridgeProduct(fridgeProduct: FridgeProduct)

    @Query("SELECT * FROM recipes")
    fun getRecipes() : Flow<List<Recipe>>

    @Query("SELECT * FROM products")
    fun getProducts() : Flow<List<Product>>

    @Query("SELECT * FROM fridge_products JOIN products ON fridge_products.product_id = products.id")
    fun getFridgeProducts() : Flow<Map<FridgeProduct, Product>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Int) : Flow<Recipe>
}