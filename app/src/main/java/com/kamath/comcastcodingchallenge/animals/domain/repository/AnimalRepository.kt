package com.kamath.comcastcodingchallenge.animals.domain.repository

import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.core.util.Resource
import kotlinx.coroutines.flow.Flow


interface AnimalRepository {
    fun getAnimals(): Flow<Resource<List<Animal>>>
    suspend fun refreshAnimals()
}