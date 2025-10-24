package com.kamath.comcastcodingchallenge.animals.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animals")
    fun getAllAnimals(): Flow<List<AnimalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<AnimalEntity>)

    @Query("DELETE FROM animals")
    suspend fun clearAllAnimals()
}
