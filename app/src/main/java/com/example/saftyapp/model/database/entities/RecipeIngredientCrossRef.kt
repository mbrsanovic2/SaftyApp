package com.example.saftyapp.model.database.entities

import androidx.room.Entity

@Entity(tableName = "recipeIngredientCrossRef", primaryKeys = ["recipeName", "ingredientName"])
data class RecipeIngredientCrossRef(
    val recipeName: String,
    val ingredientName: String,
)
