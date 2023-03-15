package com.university.bloom.ui.home

import com.university.bloom.model.Scooter

sealed interface HomeUiState {
    object Empty : HomeUiState
    object Loading : HomeUiState
    data class Successful(val items: List<Scooter>) : HomeUiState
    data class Error(val error: Throwable) : HomeUiState
}