package com.chmurka.codzisnaobiad.database

sealed interface RecipeEvent {
    object SaveRecipe: RecipeEvent
    data class SetName(val name: String): RecipeEvent
    data class SetDesc(val desc: String): RecipeEvent
    data class SetUri(val uri: String?): RecipeEvent
    object ShowDialog: RecipeEvent
    object HideDialog: RecipeEvent
    data class DeleteRecipe(val recipe: Recipe) : RecipeEvent
    data class SetCurrentRecipe(val recipe: Recipe) : RecipeEvent
}