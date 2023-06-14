package com.chmurka.codzisnaobiad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmurka.codzisnaobiad.database.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val dao: RecipeDao
) : ViewModel() {
    private val _state = MutableStateFlow(RecipeState())
    val state = combine(_state, dao.getRecipes()) { state, recipes ->
            state.copy(recipes = recipes)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RecipeState())

    fun onEvent(event: RecipeEvent) {
        when(event) {
            is RecipeEvent.DeleteRecipe -> {
                viewModelScope.launch {
                    dao.deleteRecipe(event.recipe)
                }
            }
            RecipeEvent.HideDialog -> {
                _state.update {
                    it.copy(isRecipeAdded = false)
                }
            }
            RecipeEvent.SaveRecipe -> {
                var name = state.value.name
                var desc = state.value.desc
                var uri = state.value.uri

                if( name.isBlank() || desc.isBlank() ) {
                    name = "err"
                    desc = "err"
                }
                val recipe = Recipe(
                    name = name,
                    desc = desc,
                    imageUri = uri
                )
                viewModelScope.launch {
                    dao.insertRecipe(recipe)
                }
            }
            is RecipeEvent.SetName -> _state.update {
                it.copy(name = event.name)
            }
            is RecipeEvent.SetDesc -> _state.update {
                it.copy(desc = event.desc)
            }
            is RecipeEvent.SetUri -> _state.update {
                it.copy(uri = event.uri)
            }
            RecipeEvent.ShowDialog -> _state.update {
                it.copy(isRecipeAdded = true)
            }
            is RecipeEvent.SetCurrentRecipe -> _state.update {
                it.copy(currentRecipe = event.recipe)
            }
        }
    }
}

class RecipeViewModelFactory(
    private val dao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}