package com.university.bloom.api

import com.university.bloom.model.Item
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface ItemApi {
    @GET("/space")
    suspend fun getAll(): List<Item>

    @Headers("Content-Type: application/json")
    @PUT("/space/{id}")
    suspend fun updateItem(@Path("id") itemId: Int, @Body item: Item): Item
}