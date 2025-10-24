package com.kamath.comcastcodingchallenge.animals.data.repository

import AnimalEntityMapper.toEntity
import com.kamath.comcastcodingchallenge.animals.data.local.AnimalDao
import com.kamath.comcastcodingchallenge.animals.data.local.AnimalEntity
import com.kamath.comcastcodingchallenge.animals.data.remote.api.AnimalApiService
import com.kamath.comcastcodingchallenge.animals.data.remote.dto.AnimalDto
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import com.kamath.comcastcodingchallenge.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@Singleton
class AnimalRepositoryImpl @Inject constructor(
    private val apiService: AnimalApiService,
    private val animalDao: AnimalDao
) : AnimalRepository {

    private val cacheDuration: Duration = 10.minutes

    override fun getAnimals(): Flow<Resource<List<Animal>>> = flow {
        emit(Resource.Loading())
        val cachedEntities = animalDao.getAllAnimals().first()
        val cacheIsExpired = isCacheExpired(cachedEntities)
        val shouldFetchFromApi = cachedEntities.isEmpty() || cacheIsExpired
        if (shouldFetchFromApi) {
            try {
                val animalDtos: List<AnimalDto> = listOf("dog", "bird", "bug").flatMap { name ->
                    apiService.getAnimalsByName(name)
                }
                val newEntities = animalDtos.map { it.toEntity() }
                animalDao.clearAllAnimals()
                animalDao.insertAnimals(newEntities)
                val newAnimals = newEntities.mapNotNull { AnimalEntityMapper.toDomain(it) }
                emit(Resource.Success(newAnimals))
            } catch (e: IOException) {
                val staleAnimals = cachedEntities.mapNotNull { AnimalEntityMapper.toDomain(it) }
                emit(
                    Resource.Error(
                        "Network error — showing cached results.$e",
                        staleAnimals.ifEmpty { null }
                    )
                )
            }
        } else {
            val cachedAnimals = cachedEntities.mapNotNull { AnimalEntityMapper.toDomain(it) }
            emit(Resource.Success(cachedAnimals))
        }
    }

    override suspend fun refreshAnimals() {
        try {
            val animalDtos = listOf("dog", "bird", "bug").flatMap { name ->
                apiService.getAnimalsByName(name)
            }
            val newEntities = animalDtos.map { it.toEntity() }
            animalDao.clearAllAnimals()
            animalDao.insertAnimals(newEntities)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun isCacheExpired(entities: List<AnimalEntity>): Boolean {
        if (entities.isEmpty()) return true
        val latestUpdate = entities.maxOfOrNull { it.lastUpdated } ?: 0L
        val age = (System.currentTimeMillis() - latestUpdate).milliseconds
        return age > cacheDuration
    }
}