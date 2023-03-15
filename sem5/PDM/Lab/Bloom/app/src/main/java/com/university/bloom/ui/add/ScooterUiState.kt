package com.university.bloom.ui.add

import com.university.bloom.model.Scooter

data class ScooterUiState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val item: Scooter? = null,
    val savingSuccessful: Boolean = false,
)