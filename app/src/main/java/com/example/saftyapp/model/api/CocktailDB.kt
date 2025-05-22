package com.example.saftyapp.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASEURL = "https://www.thecocktaildb.com/api/json/v1/1/"

object CocktailDB {
    fun getInstance():Retrofit{
        return Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}