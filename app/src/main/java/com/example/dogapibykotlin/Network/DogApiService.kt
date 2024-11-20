package com.example.dogapibykotlin.Network

import retrofit2.Response
import retrofit2.http.GET
import com.example.dogapibykotlin.Network.DogResponse

interface DogApiService {
    @GET("breeds/image/random")
    suspend fun getRandomImage() : Response<DogResponse>
}