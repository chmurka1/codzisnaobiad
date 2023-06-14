package com.chmurka.codzisnaobiad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chmurka.codzisnaobiad.database.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val dao: RecipeDao
) : ViewModel() {
    private val _state = MutableStateFlow(ProductState())
    val state = combine(_state, dao.getProducts()) { state, products ->
        state.copy(products = products)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductState())

    fun onEvent(event: ProductEvent) {
        when(event) {
            is ProductEvent.DeleteProduct -> {
                viewModelScope.launch {
                    dao.deleteProduct(event.product)
                }
            }
            is ProductEvent.HideDialog -> {
                _state.update {
                    it.copy(isProductAdded = false)
                }
            }
            is ProductEvent.SaveProduct -> {
                var name = state.value.name
                if( name.isBlank() ) {
                    return
                }
                val product = Product(
                    name = name,
                    proteins100g = state.value.proteins,
                    fat100g = state.value.fat,
                    carbohydrates100g = state.value.carbs,
                    energyKcal = state.value.energyKcal
                )
                viewModelScope.launch {
                    dao.insertProduct(product)
                }
            }
            is ProductEvent.SetName -> _state.update {
                it.copy(name = event.name)
            }
            ProductEvent.ShowDialog -> _state.update {
                it.copy(isProductAdded = true)
            }
            is ProductEvent.SetCurrentProduct -> _state.update {
                it.copy(currentProduct = event.product)
            }
            is ProductEvent.SetCarbs -> _state.update {
                it.copy(carbs = event.value)
            }
            is ProductEvent.SetFat -> _state.update {
                it.copy(fat = event.value)
            }
            is ProductEvent.SetKcal -> _state.update {
                it.copy(energyKcal = event.value.toFloat())
            }
            is ProductEvent.SetProteins -> _state.update {
                it.copy(proteins = event.value)
            }
        }
    }
}

class ProductViewModelFactory(
    private val dao: RecipeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}