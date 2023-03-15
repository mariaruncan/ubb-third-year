package com.university.bloom.api

import com.university.bloom.api.requests.LoginRequest
import com.university.bloom.api.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @Headers("Content-Type: application/json")
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}