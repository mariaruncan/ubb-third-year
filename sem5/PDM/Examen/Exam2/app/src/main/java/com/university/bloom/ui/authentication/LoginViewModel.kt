package com.university.bloom.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val loading: Boolean = false,
    val loginSuccessful: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    init {
        viewModelScope.launch {
            userPreferencesRepo.username.collect {
                Log.d("USERNAME", it)
                if (it.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(loginSuccessful = true)
                }
            }
        }
    }

    fun onLoginClick(username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            userPreferencesRepo.setUsername(username)
            _uiState.value = _uiState.value.copy(loading = false, loginSuccessful = true)
        }
    }
}