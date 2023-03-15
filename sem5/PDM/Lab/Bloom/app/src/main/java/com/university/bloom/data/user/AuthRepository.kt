package com.university.bloom.data.user

import com.university.bloom.api.responses.LoginResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authDataSource: RemoteAuthDataSource
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return authDataSource.login(username, password)
    }
}