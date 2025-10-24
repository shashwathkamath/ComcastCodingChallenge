package com.kamath.comcastcodingchallenge

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kamath.comcastcodingchallenge.animals.data.AnimalFeatureRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var workManager: WorkManager
    override fun onCreate() {
        super.onCreate()
        schedulePeriodicDataRefresh()
    }

    private fun schedulePeriodicDataRefresh() {
        Log.d("App", "schedulePeriodicDataRefresh: called")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val refreshRequest = PeriodicWorkRequestBuilder<AnimalFeatureRefreshWorker>(
            15,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            AnimalFeatureRefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
        Log.d("App", "Periodic refresh worker enqueued with network constraints.")
    }
}