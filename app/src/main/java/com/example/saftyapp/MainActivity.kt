package com.example.saftyapp

import com.example.saftyapp.model.ReminderWorker
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.saftyapp.model.viewmodels.MainViewModel
import com.example.saftyapp.navigation.Navigation
import com.example.saftyapp.ui.theme.SaftyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import androidx.core.content.edit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveLastOpenTime(this)
        scheduleReminderWorker(this)

        runBlocking {
            viewModel.initializeApp()
        }

        enableEdgeToEdge()
        setContent {
            SaftyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    // Saves last time app was opened in shared Preferences
    private fun saveLastOpenTime(context: Context) {
        val prefs = context.getSharedPreferences("safty_prefs", Context.MODE_PRIVATE)
        prefs.edit { putLong("last_open_time", System.currentTimeMillis()) }
    }

    private fun scheduleReminderWorker(context: Context) {
        // Schedules a worker that runs ONCE every 24h
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "safty_reminder_check",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}