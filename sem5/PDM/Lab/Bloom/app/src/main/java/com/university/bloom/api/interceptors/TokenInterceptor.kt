package com.university.bloom.api.interceptors

import android.util.Log
import com.university.bloom.data.userpreferences.UserPreferencesRepository
import com.university.bloom.utils.TAG
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { userPreferencesRepository.token.first() }
        Log.d(TAG, "token value: $token")
        return if (token.isNotEmpty()) {
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(chain.request())
        }
    }
}