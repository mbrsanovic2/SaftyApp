package com.example.saftyapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.saftyapp.model.Objects.ArchiveEntry
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.database.Converters
import com.example.saftyapp.model.database.RecipeDatabase
import com.example.saftyapp.model.database.Repository
import com.example.saftyapp.navigation.Navigation
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val db = RecipeDatabase.getDatabase(applicationContext)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaftyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    //TODO- init fun
}