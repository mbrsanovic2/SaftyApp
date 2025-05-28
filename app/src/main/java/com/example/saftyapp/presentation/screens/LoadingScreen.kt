package com.example.saftyapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.saftyapp.model.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait

@Composable
fun LoadingScreen(
    viewmodel: MainViewModel = hiltViewModel(),
    onLoad: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewmodel.loadFromApi(onLoad)
    }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentLoad = viewmodel.currentLoadProgress.collectAsState()
            val maxLoad = viewmodel.maxLoadProgress.collectAsState()
            val progress = currentLoad.value.toFloat() / maxLoad.value.toFloat()

            Text("Loading Data. Please wait")
            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(modifier = Modifier.height(32.dp), progress = { progress })
            Spacer(modifier = Modifier.height(16.dp))

            Text("Loaded " + currentLoad.value.toString() + " of " + maxLoad.value)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
