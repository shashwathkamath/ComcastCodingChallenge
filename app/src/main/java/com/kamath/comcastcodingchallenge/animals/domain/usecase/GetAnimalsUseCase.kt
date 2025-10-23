package com.kamath.comcastcodingchallenge.animals.domain.usecase

import com.kamath.comcastcodingchallenge.animals.domain.model.Animal
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import com.kamath.comcastcodingchallenge.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetAnimalsUseCase @Inject constructor(
    private val repository: AnimalRepository
) {
    operator fun invoke(): Flow<Resource<List<Animal>>> {
        return repository.getAnimals()
            .onStart {
                repository.refreshAnimals()
            }
    }
}