package com.university.bloom.api

import com.university.bloom.model.Scooter
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ScooterApi {
    @GET("/api/item")
    suspend fun getAll(): List<Scooter>

    @GET("/api/item/{id}")
    suspend fun getById(@Path("id") itemId: String): Scooter

    @Headers("Content-Type: application/json")
    @POST("/api/item")
    suspend fun addItem(@Body item: Scooter): Scooter

    @Headers("Content-Type: application/json")
    @PUT("/api/item/{id}")
    suspend fun updateItem(@Path("id") itemId: String, @Body item: Scooter): Scooter
}