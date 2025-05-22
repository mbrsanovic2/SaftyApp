package com.example.saftyapp.model.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("filter.php")
    suspend fun getDrinksByIngredient(@Query("i") ingredient: String): Response<GetDrinksResponse>

    @GET("lookup.php")
    suspend fun getDrinkDetails(@Query("i") id: Int): Response<GetDrinkDetailResponse>
}