package com.chmurka.codzisnaobiad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmurka.codzisnaobiad.database.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FridgeViewModel(
    private val dao: RecipeDao
) : ViewModel() {
    private val _state = MutableStateFlow(FridgeProductState())
    val state = combine(_state, dao.getFridgeProducts()) { state, fridgeProducts ->
        state.copy(fridgeProducts = fridgeProducts)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FridgeProductState())

    fun onEvent(event: FridgeProductEvent) {
        when(event) {
            is FridgeProductEvent.DeleteFridgeProduct -> {
                viewModelScope.launch {
                    dao.deleteFridgeProduct(event.product)
                }
            }
            is FridgeProductEvent.HideDialog -> {
                _state.update {
                    it.copy(isFridgeProductAdded = false)
                }
            }
            is FridgeProductEvent.SaveFridgeProduct -> {
                val id: Int = state.value.product ?: -1
                val fridgeProduct = FridgeProduct(
                    product_id = id,
                    expirationDate = state.value.expirationDate,
                    amount_g = state.value.amount,
                )
                viewModelScope.launch {
                    dao.insertFridgeProduct(fridgeProduct)
                }
            }
            is FridgeProductEvent.SetProduct -> _state.update {
                it.copy(product = event.id)
            }
            is FridgeProductEvent.SetExpirationDate -> _state.update {
                it.copy(expirationDate = event.date)
            }
            is FridgeProductEvent.SetAmount -> _state.update {
                it.copy(amount = event.value)
            }
            is FridgeProductEvent.ShowDialog -> _state.update {
                it.copy(isFridgeProductAdded = true)
            }
            is FridgeProductEvent.SetCurrentFridgeProduct -> _state.update {
                it.copy(currentFridgeProduct = event.fridgeProduct)
            }
        }
    }
}

class FridgeViewModelFactory(
    private val dao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FridgeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FridgeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}