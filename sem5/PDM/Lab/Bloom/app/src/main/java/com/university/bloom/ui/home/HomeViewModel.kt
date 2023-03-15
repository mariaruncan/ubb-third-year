package com.university.bloom.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.bloom.data.scooter.ScooterRepository
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import com.university.bloom.model.Scooter
import com.university.bloom.services.ConnectionMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scooterRepo: ScooterRepository,
    private val userPreferencesRepo: UserPreferencesRepository,
    private val connectionMonitor: ConnectionMonitor
) : ViewModel() {
    private val _items: MutableStateFlow<List<Scooter>> = MutableStateFlow(emptyList())
    val items: Flow<List<Scooter>> get() = _items

    val connectionState: Flow<Boolean> get() = connectionMonitor.isOnline

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _items.value = scooterRepo.getAll() ?: emptyList()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            userPreferencesRepo.setToken("")
        }
    }
}