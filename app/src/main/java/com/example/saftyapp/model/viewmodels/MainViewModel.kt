package com.example.saftyapp.model.viewmodels

import android.util.Log
import android.util.MutableBoolean
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.saftyapp.model.api.APIInterface
import com.example.saftyapp.model.api.CocktailDB
import com.example.saftyapp.model.api.Drink
import com.example.saftyapp.model.api.DrinkDetails
import com.example.saftyapp.model.api.GetDrinksResponse
import com.example.saftyapp.model.database.Repository
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.navigation.Screens
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var maxLoadProgress = MutableStateFlow(0)
    var currentLoadProgress = MutableStateFlow(0)
    private val databaseState = MutableStateFlow(true)

    suspend fun initializeApp() {
            repository.loadDefaultData()
    }

    suspend fun loadFromApi(onFinish: () -> Unit) {
        runBlocking {
            databaseState.value = repository.RecipeFunctions().getState()
        }
        if (!databaseState.value) {
            val recipesIds = repository.getAPIRecipeIds()
            setMaxLoad(recipesIds.size)

            //TODO Remove Recipes already in Database -> in Repository
            //TODO Save Recipe immediately upon getting them
            val recipes = repository.getAPIRecipes(
                recipesIds,
                increaseFunction = { currentLoadProgress.value++ })
            recipes.forEach { rec ->
                repository.RecipeFunctions().addRecipe(rec)
            }
        }
        onFinish()
    }

    private fun setMaxLoad(value: Int) {
        maxLoadProgress.value = value
    }
}