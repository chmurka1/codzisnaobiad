package com.chmurka.codzisnaobiad.database

data class RecipeState(
    val recipes: List<Recipe> = emptyList(),
    val currentRecipe: Recipe = Recipe(),
    val name: String = "",
    val desc: String = "",
    val uri: String? = null,
    val isRecipeAdded: Boolean = false
)
