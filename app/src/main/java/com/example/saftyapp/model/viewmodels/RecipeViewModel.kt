package com.example.saftyapp.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.RecipeWithIngredientsEntity
import com.example.saftyapp.model.getTestRecipes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() { // TODO Repository einbinden
    // Recipes from database
    private val _recipes = MutableStateFlow<List<RecipeWithIngredientsEntity>>(emptyList())
    val recipes = _recipes.asStateFlow()

    // Ingredients from database
    private val _ingredients = MutableStateFlow<List<IngredientEntity>>(emptyList())
    val ingredients = _ingredients.asStateFlow()

    // Selected ingredients in HomeScreen or RecipeScreen
    private val _selectedIngredients = mutableStateListOf<IngredientEntity>()
    val selectedIngredients: List<IngredientEntity> get() = _selectedIngredients

    // Filtering in RecipeScreen
    private val _queryText = MutableStateFlow("")
    val queryText = _queryText.asStateFlow()
    private val _filteredRecipes = MutableStateFlow<List<RecipeWithIngredientsEntity>>(emptyList())
    val filteredRecipes = _filteredRecipes.asStateFlow()

    init {
        loadIngredientsFromDB()
        loadMockRecipes()
    }

    fun loadIngredientsFromDB() {
        // TODO - vorerst simuliertes Laden
        val dbIngredients = listOf(
            IngredientEntity(0,"Strawberry", "https://www.thecocktaildb.com/images/ingredients/strawberries-small.png", Color.Red, true),
            IngredientEntity(1,"Apple", "https://www.thecocktaildb.com/images/ingredients/apple-small.png", Color.Red, true),
            IngredientEntity(2,"Pineapple", "https://www.thecocktaildb.com/images/ingredients/pineapple-small.png", Color.Yellow, true),
            IngredientEntity(3,"Water", "https://www.thecocktaildb.com/images/ingredients/water-small.png", Color.Transparent, true),
            IngredientEntity(4,"Kiwi", "https://www.thecocktaildb.com/images/ingredients/kiwi-small.png", Color.Green, true),
            IngredientEntity(5,"Papaya", "https://www.thecocktaildb.com/images/ingredients/papaya-small.png", Color.Yellow, true),
            IngredientEntity(6,"Berries", "https://www.thecocktaildb.com/images/ingredients/berries-small.png", Color.Red, true),
            IngredientEntity(7,"Coffee", "https://www.thecocktaildb.com/images/ingredients/coffee-small.png", Color.Black, true),
            IngredientEntity(8,"Milk", "https://www.thecocktaildb.com/images/ingredients/milk-small.png", Color.White, true),
            IngredientEntity(9,"Ice", "https://www.thecocktaildb.com/images/ingredients/ice-small.png", Color.Transparent, true),
            IngredientEntity(10,"Orange", "https://www.thecocktaildb.com/images/ingredients/orange-small.png", Color.Yellow, false)
        )
        _ingredients.value = dbIngredients
    }

    fun selectIngredient(ingredient: IngredientEntity) {
        _selectedIngredients.add(ingredient)
        updateFilteredRecipes()
    }

    fun deselectIngredient(ingredient: IngredientEntity) {
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
            val matchesQuery = recipe.recipe.name.contains(query, ignoreCase = true)
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

        var ingredientIdCounter = 0

        val mockedRecipes = testRecipes.map { recipe ->
            val recipeEntity = RecipeEntity(
                id = recipe.id,
                name = recipe.name,
                isCustom = recipe.isCustom,
                isAlcoholic = recipe.alcoholic,
                instructions = recipe.instructions,
                thumbnail = recipe.thumbnail
            )

            val ingredients = recipe.ingredients.map { ingredientName ->
                IngredientEntity(
                    id = ingredientIdCounter++,
                    name = ingredientName,
                    color = Color.Cyan,
                    isUnlocked = true
                )
            }

            RecipeWithIngredientsEntity(
                recipe = recipeEntity,
                ingredients = ingredients
            )
        }

        _recipes.value = mockedRecipes
        updateFilteredRecipes()
    }
}