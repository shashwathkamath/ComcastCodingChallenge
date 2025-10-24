package com.kamath.comcastcodingchallenge.di

import android.content.Context
import androidx.room.Room
import com.kamath.comcastcodingchallenge.animals.data.local.AnimalDao
import com.kamath.comcastcodingchallenge.animals.data.local.AnimalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AnimalDatabase {
        return Room.databaseBuilder(
                context,
                AnimalDatabase::class.java,
                "animal_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideAnimalDao(database: AnimalDatabase): AnimalDao {
        return database.animalDao()
    }
}
