package com.kamath.comcastcodingchallenge.animals.di

import com.kamath.comcastcodingchallenge.animals.data.repository.AnimalRepositoryImpl
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnimalRepositoryBinder {
    @Binds
    @Singleton
    abstract fun bindAnimalRepository(
        animalRepositoryImpl: AnimalRepositoryImpl
    ): AnimalRepository
}