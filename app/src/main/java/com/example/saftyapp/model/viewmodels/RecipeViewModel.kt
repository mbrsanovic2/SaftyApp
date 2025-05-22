package com.example.saftyapp.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.model.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel()  {
    // Recipes from database
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    // Ingredients from database
    private val _unlockedIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val unlockedIngredients = _unlockedIngredients.asStateFlow()
    private val _allIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val allIngredients = _allIngredients.asStateFlow()

    // Selected ingredients in HomeScreen or RecipeScreen
    private val _selectedIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients: List<Ingredient> get() = _selectedIngredients

    // Filtering in RecipeScreen
    private val _queryText = MutableStateFlow("")
    val queryText = _queryText.asStateFlow()

    private val _filteredRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val filteredRecipes = _filteredRecipes.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe = _selectedRecipe.asStateFlow()

    init {
        viewModelScope.launch {
            loadIngredients()
            loadRecipes()
        }
    }

    private suspend fun loadRecipes() {
        _recipes.value = repository.RecipeFunctions()
            .getAllRecipes()
            .sortedBy { it.name.lowercase() }

        updateFilteredRecipes()
    }

    private suspend fun loadIngredients() {
        _allIngredients.value = repository.RecipeFunctions()
            .getAllIngredients()
            .sortedBy { it.name.lowercase() }

        _unlockedIngredients.value = allIngredients.value.filter { it.isUnlocked }
        // TODO - vorerst simuliertes Laden -> für visual cue testing gut ;)
//        val dbIngredients = listOf(
//            Ingredient("Strawberry", "https://www.thecocktaildb.com/images/ingredients/strawberries-small.png", Color.Red, true, true),
//            Ingredient("Apple", "https://www.thecocktaildb.com/images/ingredients/apple-small.png", Color.Red, true, false),
//            Ingredient("Pineapple", "https://www.thecocktaildb.com/images/ingredients/pineapple-small.png", Color.Yellow, true, true),
//            Ingredient("Water", "https://www.thecocktaildb.com/images/ingredients/water-small.png", Color.Transparent, true, true),
//            Ingredient("Kiwi", "https://www.thecocktaildb.com/images/ingredients/kiwi-small.png", Color.Green, true),
//            Ingredient("Papaya", "https://www.thecocktaildb.com/images/ingredients/papaya-small.png", Color.Yellow, true),
//            Ingredient("Berries", "https://www.thecocktaildb.com/images/ingredients/berries-small.png", Color.Red, true),
//            Ingredient("Coffee", "https://www.thecocktaildb.com/images/ingredients/coffee-small.png", Color.Black, true),
//            Ingredient("Milk", "https://www.thecocktaildb.com/images/ingredients/milk-small.png", Color.White, true),
//            Ingredient("Ice", "https://www.thecocktaildb.com/images/ingredients/ice-small.png", Color.Transparent, true),
//            Ingredient("Ham", null, Color.Gray, true),
//            Ingredient("Orange", "https://www.thecocktaildb.com/images/ingredients/orange-small.png", Color.Yellow, false)
//        )
//        _unlockedIngredients.value = dbIngredients
    }

    fun selectIngredient(ingredient: Ingredient) {
        _selectedIngredients.add(ingredient)
        updateFilteredRecipes()
    }

    fun deselectIngredient(ingredient: Ingredient) {
        _selectedIngredients.remove(ingredient)
        updateFilteredRecipes()
    }

    fun deselectAllIngredients() {
        _selectedIngredients.clear()
    }

    fun setSelectedRecipe(recipeName: String) {
        val recipe = _filteredRecipes.value.find { it.name == recipeName }
        _selectedRecipe.value = recipe
    }

    fun clearFilter() {
        deselectAllIngredients()
        updateFilteredRecipes()
        updateQuery("")
    }

    private fun updateFilteredRecipes() {
        val currentRecipes = _recipes.value
        val query = _queryText.value

        val filtered = currentRecipes.filter { recipe ->
            val matchesQuery = recipe.name.contains(query, ignoreCase = true)
            val matchesIngredients = _selectedIngredients.isEmpty() || _selectedIngredients.all { selected ->
                recipe.keyIngredients.any { it.name.equals(selected.name, ignoreCase = true) }
            }
            matchesQuery && matchesIngredients
        }

        _filteredRecipes.value = filtered
    }

    fun updateQuery(newQuery: String) {
        _queryText.value = newQuery
        updateFilteredRecipes()
    }

    fun scoreRecipe(recipeName: String) {
        viewModelScope.launch {
            repository.RecipeFunctions().finishRecipe(recipeName)
            loadRecipes()
        }
    }

    fun updateRecentlyUnlocked(unlockedIngredients: List<Ingredient>) {
        val currentList = _unlockedIngredients.value
        val updatedUnlocked = unlockedIngredients.map { it.copy(recentlyUnlocked = true) }
        val updatedList = currentList + updatedUnlocked
        _unlockedIngredients.value = updatedList.sortedBy { it.name.lowercase() }
    }

    suspend fun addRecipe(recipeName: String, ingredients: List<String>, preparation: String){
        val matchingIngredients = _unlockedIngredients.value.filter { it.name in ingredients }
        repository.RecipeFunctions().addRecipe(Recipe(
            name = recipeName,
            instructions = preparation,
            keyIngredients = matchingIngredients,
            allIngredients = ingredients,
            isCustom = true
        ))
        loadRecipes()
    }
}