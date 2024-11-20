package com.example.dogapibykotlin.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    private const val BASE_URL = "https://dog.ceo/api/"
    fun getApiService(): DogApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiService :: class.java)
    }
}