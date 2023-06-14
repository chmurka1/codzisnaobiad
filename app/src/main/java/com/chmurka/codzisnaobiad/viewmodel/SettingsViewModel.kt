package com.chmurka.codzisnaobiad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: DataStoreRepository) : ViewModel() {
    val settings = settingsRepository.settingsFlow

    fun setDarkTheme(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkTheme(value)
        }
    }
}

class SettingsViewModelFactory(
    private val settingsRepository: DataStoreRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}