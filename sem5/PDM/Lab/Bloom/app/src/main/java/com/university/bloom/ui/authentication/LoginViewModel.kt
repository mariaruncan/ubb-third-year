package com.university.bloom.ui.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.university.bloom.data.user.AuthRepository
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    init {
        viewModelScope.launch {
            userPreferencesRepo.token.collect {
                if (it.isNotEmpty()) {
                    _uiState.value = _uiState.value.toggleLoginEnded(isSuccessful = true)
                }
            }
        }
    }

    fun onLoginClick(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.toggleBeginLogin()
            val result = authRepo.login(username, password)
            if (result.isSuccess) {
                userPreferencesRepo.setToken(result.getOrNull()?.token ?: "")
                _uiState.value = _uiState.value.toggleLoginEnded(isSuccessful = true)
            } else {
                _uiState.value = _uiState.value.toggleErrorWhileLogin(exception = result.exceptionOrNull())
            }
        }
    }

    fun onErrorConsumed() {
        viewModelScope.launch {
            delay(1000L)
            _uiState.value = _uiState.value.resetState()
        }
    }
}