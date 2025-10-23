package com.kamath.comcastcodingchallenge

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
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
        val refreshRequest = PeriodicWorkRequestBuilder<AnimalFeatureRefreshWorker>(
            15,
            TimeUnit.MINUTES
        ).build()
        workManager.enqueueUniquePeriodicWork(
            AnimalFeatureRefreshWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
    }
}