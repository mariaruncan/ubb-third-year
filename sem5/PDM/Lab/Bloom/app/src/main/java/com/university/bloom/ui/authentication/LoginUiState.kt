package com.university.bloom.ui.authentication

data class LoginUiState(
    val loading: Boolean = false,
    val loginSuccessful: Boolean = false,
    val error: Throwable? = null
) {
    fun toggleBeginLogin() : LoginUiState = this.copy(
        loading = true,
        loginSuccessful = false
    )

    fun toggleLoginEnded(isSuccessful: Boolean): LoginUiState = this.copy(
        loading = false,
        loginSuccessful = isSuccessful
    )

    fun toggleErrorWhileLogin(exception: Throwable?) : LoginUiState = this.copy(
        loading = false,
        loginSuccessful = false,
        error = exception
    )

    fun resetState() : LoginUiState = this.copy(
        loading = false,
        loginSuccessful = false,
        error = null
    )
}