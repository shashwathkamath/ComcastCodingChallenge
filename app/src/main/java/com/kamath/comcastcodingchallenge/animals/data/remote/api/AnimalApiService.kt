package com.kamath.comcastcodingchallenge.animals.data.remote.api

import com.kamath.comcastcodingchallenge.animals.data.remote.dto.AnimalDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalApiService {
    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/v1/"
        const val API_KEY = "XB2xaIrruYztlhnd+h3yCw==bvpvbo1A33T3ptEn"
    }

    @GET("animals")
    suspend fun getAnimalsByName(
        @Query("name") animalName: String
    ): List<AnimalDto>
}