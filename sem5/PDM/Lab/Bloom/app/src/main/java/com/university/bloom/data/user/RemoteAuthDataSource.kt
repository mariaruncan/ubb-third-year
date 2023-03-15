package com.university.bloom.data.user

import com.university.bloom.api.AuthApi
import com.university.bloom.api.requests.LoginRequest
import com.university.bloom.api.responses.LoginResponse
import javax.inject.Inject

class RemoteAuthDataSource @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> =
        try {
            Result.success(authApi.login(LoginRequest(username = username, password = password)))
        } catch (e: Exception) {
            Result.failure(e)
        }
}