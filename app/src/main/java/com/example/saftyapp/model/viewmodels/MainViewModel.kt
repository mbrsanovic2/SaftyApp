package com.example.saftyapp.model.viewmodels

import androidx.lifecycle.ViewModel
import com.example.saftyapp.model.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
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
            getDatabaseState()
        }

        val foundDrinks = repository.RecipeFunctions().getAllRecipes()

        val apiDrinkList = repository.getAPIRecipeIds()
        if (apiDrinkList.size > foundDrinks.size) {
            setMaxLoad(apiDrinkList.size - foundDrinks.size)

            repository.getAPIRecipes(
                apiDrinkList.filter { drink -> !foundDrinks.any { x -> x.name == drink.strDrink } },
                increaseFunction = { currentLoadProgress.value++ }
            )
        }
        onFinish()
    }

    suspend fun getDatabaseState(): Boolean {
        databaseState.value = repository.RecipeFunctions().getAllRecipes().size >= 550
        return databaseState.value
    }

    private fun setMaxLoad(value: Int) {
        maxLoadProgress.value = value
    }
}