package com.kamath.comcastcodingchallenge.animals.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kamath.comcastcodingchallenge.animals.domain.repository.AnimalRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import jakarta.inject.Inject

@HiltWorker
class AnimalFeatureRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    @Inject
    internal lateinit var repository: AnimalRepository

    override suspend fun doWork(): Result {
        Log.d("AnimalFeatureRefreshWorker", "doWork: called")
        return try {
            repository.refreshAnimals()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AnimalFeatureRefreshWorker"
    }
}