package com.kamath.comcastcodingchallenge.animals.data.repository

import android.icu.util.TimeUnit
import android.util.Log
import android.util.LruCache
import com.kamath.comcastcodingchallenge.animals.data.remote.api.AnimalApiService
import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import com.kamath.comcastcodingchallenge.core.util.Constants
import com.kamath.comcastcodingchallenge.core.util.Resource
import com.kamath.comcastcodingchallenge.core.util.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private data class CacheEntry(
    val data: List<Animal>,
    val timestamp: Long
)

@Singleton
class AnimalRepositoryImpl @Inject constructor(
    private val apiService: AnimalApiService
) : AnimalRepository {
    private val animalCache = LruCache<String, CacheEntry>(3)
    private val CACHE_KEY = Constants.CACHE_KEY
    private val animalNames = listOf("dog", "bird", "bug")
    private val _animalsFlow = MutableStateFlow<Resource<List<Animal>>>(Resource.Loading())
    private val cacheDurationMs = 10 * 60 * 1000L

    override fun getAnimals(): Flow<Resource<List<Animal>>> = _animalsFlow.asStateFlow()

    override suspend fun refreshAnimals() {
        val cachedEntry = animalCache.get(CACHE_KEY)
        val isCacheValid = cachedEntry != null && !isCacheExpired(cachedEntry.timestamp)
        if (isCacheValid && _animalsFlow.value is Resource.Success) {
            Log.d("AnimalRepository", "Skipping refresh, valid cache is present.")
            _animalsFlow.value = Resource.Success(cachedEntry!!.data)
            return
        }
        Log.d("AnimalRepository", "Starting to refresh animals from API...")
        if (_animalsFlow.value !is Resource.Success) {
            _animalsFlow.value = Resource.Loading()
        }
        try {
            val allAnimals = animalNames.flatMap { name ->
                apiService.getAnimalsByName(name).mapNotNull { it.toDomain() }
            }
            animalCache.put(
                CACHE_KEY, CacheEntry(
                    data = allAnimals,
                    timestamp = System.currentTimeMillis()
                )
            )
            _animalsFlow.value = Resource.Success(allAnimals)
            Log.d(
                "AnimalRepository",
                "Successfully refreshed data from API. Cache and flow updated."
            )
        } catch (e: IOException) {
            Log.e("AnimalRepository", "Network error during refresh: ${e.message}")
            _animalsFlow.value = Resource.Error(
                "No Internet Connection. Displaying cached data.",
                _animalsFlow.value.data
            )
        } catch (e: Exception) {
            Log.e("AnimalRepository", "Generic error during refresh: ${e.message}")
            _animalsFlow.value = Resource.Error(
                "An unexpected error occurred. Displaying cached data.",
                _animalsFlow.value.data
            )
        }
    }

    private fun isCacheExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > cacheDurationMs
    }
}