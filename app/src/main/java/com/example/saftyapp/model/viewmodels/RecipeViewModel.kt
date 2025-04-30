package com.example.saftyapp.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.Objects.RecipeStruct
import com.example.saftyapp.model.getTestRecipes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() { // TODO Repository einbinden
    // Recipes from database
    private val _recipes = MutableStateFlow<List<RecipeStruct>>(emptyList())
    val recipes = _recipes.asStateFlow()

    // Ingredients from database
    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients = _ingredients.asStateFlow()

    // Selected ingredients in HomeScreen or RecipeScreen
    private val _selectedIngredients = mutableStateListOf<Ingredient>()
    val selectedIngredients: List<Ingredient> get() = _selectedIngredients

    // Filtering in RecipeScreen
    private val _queryText = MutableStateFlow("")
    val queryText = _queryText.asStateFlow()
    private val _filteredRecipes = MutableStateFlow<List<RecipeStruct>>(emptyList())
    val filteredRecipes = _filteredRecipes.asStateFlow()

    init {
        loadIngredientsFromDB()
        loadMockRecipes()
    }

    fun loadIngredientsFromDB() {
        // TODO - vorerst simuliertes Laden
        val dbIngredients = listOf(
            Ingredient("Strawberry", "https://www.thecocktaildb.com/images/ingredients/strawberries-small.png", Color.Red, true),
            Ingredient("Apple", "https://www.thecocktaildb.com/images/ingredients/apple-small.png", Color.Red, true),
            Ingredient("Pineapple", "https://www.thecocktaildb.com/images/ingredients/pineapple-small.png", Color.Yellow, true),
            Ingredient("Water", "https://www.thecocktaildb.com/images/ingredients/water-small.png", Color.Transparent, true),
            Ingredient("Kiwi", "https://www.thecocktaildb.com/images/ingredients/kiwi-small.png", Color.Green, true),
            Ingredient("Papaya", "https://www.thecocktaildb.com/images/ingredients/papaya-small.png", Color.Yellow, true),
            Ingredient("Berries", "https://www.thecocktaildb.com/images/ingredients/berries-small.png", Color.Red, true),
            Ingredient("Coffee", "https://www.thecocktaildb.com/images/ingredients/coffee-small.png", Color.Black, true),
            Ingredient("Milk", "https://www.thecocktaildb.com/images/ingredients/milk-small.png", Color.White, true),
            Ingredient("Ice", "https://www.thecocktaildb.com/images/ingredients/ice-small.png", Color.Transparent, true),
            Ingredient("Ham", null, Color.Gray, true),
            Ingredient("Orange", "https://www.thecocktaildb.com/images/ingredients/orange-small.png", Color.Yellow, false)
        )
        _ingredients.value = dbIngredients
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

    fun clearFilter() {
        deselectAllIngredients()
        updateFilteredRecipes()
        updateQuery("")
    }

    fun updateFilteredRecipes() {
        val currentRecipes = _recipes.value
        val query = _queryText.value

        val filtered = currentRecipes.filter { recipe ->
            val matchesQuery = recipe.name.contains(query, ignoreCase = true)
            val matchesIngredients = _selectedIngredients.isEmpty() || _selectedIngredients.all { selected ->
                recipe.ingredients.any { it.name.equals(selected.name, ignoreCase = true) }
            }
            matchesQuery && matchesIngredients
        }

        _filteredRecipes.value = filtered
    }

    fun updateQuery(newQuery: String) {
        _queryText.value = newQuery
        updateFilteredRecipes()
    }

    private fun loadMockRecipes() {
        val testRecipes = getTestRecipes()

        val mockedRecipes = testRecipes.map { recipe ->
            RecipeStruct(
                name = recipe.name,
                instructions = recipe.instructions,
                thumbnail = recipe.thumbnail,
                isCustom = recipe.isCustom,
                isAlcoholic = recipe.alcoholic,
                ingredients = recipe.ingredients.map { ingredientName ->
                    Ingredient(
                        name = ingredientName,
                        color = Color.Cyan,
                        isUnlocked = true
                    )
                }
            )
        }

        _recipes.value = mockedRecipes
        updateFilteredRecipes()
    }
}