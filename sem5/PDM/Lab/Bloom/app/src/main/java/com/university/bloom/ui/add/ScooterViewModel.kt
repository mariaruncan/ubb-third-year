package com.university.bloom.ui.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.bloom.data.scooter.ScooterRepository
import com.university.bloom.services.ConnectionMonitor
import com.university.bloom.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScooterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val scooterRepo: ScooterRepository,
    private val connectionMonitor: ConnectionMonitor
) : ViewModel() {
    private val itemId: String = savedStateHandle["itemId"]!!

    private var _uiState: MutableStateFlow<ScooterUiState> = MutableStateFlow(ScooterUiState())
    val uiState: StateFlow<ScooterUiState> get() = _uiState

    val connectionState: Flow<Boolean> get() = connectionMonitor.isOnline

    init {
        loadItem()
    }

    private fun loadItem() {
        Log.d(TAG, "itemId: $itemId")
        if (itemId != "+") {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val item = scooterRepo.getItemById(itemId)
                if (item == null) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = Exception("Can not load item!"))
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, item = item)
                }
            }
        }
    }

    fun saveItem(number: Int, batteryLevel: Int, locked: Boolean, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = if (itemId == "+") {
                scooterRepo.add(number, batteryLevel, locked, latitude, longitude)
            } else {
                scooterRepo.updateItem(itemId, number, batteryLevel, locked, latitude, longitude)
            }
            if (result == null) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = Exception("Can not save!"))
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, savingSuccessful = true)
            }
        }
    }

    fun onErrorConsumed() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}