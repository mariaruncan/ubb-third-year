package com.university.bloom.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.bloom.data.item.ItemRepository
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import com.university.bloom.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val items: List<Item> = emptyList(),
    val shouldRetry: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemRepo: ItemRepository,
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {
    private val _items: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())

    var username: String = ""

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferencesRepo.username.collect {
                Log.d("USERNAME", it)
                username = it
            }
        }
        loadItems()
        viewModelScope.launch {
            itemRepo.itemsStream.collect { list ->
                _items.value = list
                _uiState.value = _uiState.value.copy(items = list)
            }
        }
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, shouldRetry = false)
            val isSuccess = itemRepo.fetchData()
            if (!isSuccess) {
                _uiState.value = _uiState.value.copy(loading = false, shouldRetry = true, error = Exception("Something went wrong"))
            } else {
                _uiState.value = _uiState.value.copy(loading = false)
            }
        }
    }

    fun onReleaseClick(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val item = _items.value.firstOrNull { it.id == id }
            if (item != null) {
                val requestItem = item.copy(status = "free", takenBy = "")
                val response = itemRepo.updateItem(requestItem)
                if (response != null) {
                    _uiState.value = _uiState.value.copy(loading = false)
                } else {
                    _uiState.value = _uiState.value.copy(loading = false, error = Exception("Something went wrong, try again"))
                }
            } else {
                _uiState.value = _uiState.value.copy(loading = false, error = Exception("Can not find item with id $id"))
            }
        }
    }

    fun onTakeClick(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val item = _items.value.firstOrNull { it.id == id }
            if (item != null) {
                val requestItem = item.copy(status = "taken", takenBy = username)
                val response = itemRepo.updateItem(requestItem)
                if (response != null) {
                    _uiState.value = _uiState.value.copy(loading = false)
                } else {
                    _uiState.value = _uiState.value.copy(loading = false, error = Exception("Something went wrong, try again"))
                }
            } else {
                _uiState.value = _uiState.value.copy(loading = false, error = Exception("Can not find item with id $id"))
            }
        }
    }

    fun onErrorConsumed() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}