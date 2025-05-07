package com.example.saftyapp.model.database.entities

import androidx.room.Entity

@Entity(tableName = "recipeIngredientCrossRef", primaryKeys = ["recipeID", "ingredientID"])
data class RecipeIngredientCrossRef(
    val recipeID: Int,
    val ingredientID: Int,
)
