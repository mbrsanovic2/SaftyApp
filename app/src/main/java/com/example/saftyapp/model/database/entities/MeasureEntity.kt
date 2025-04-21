package com.example.saftyapp.model.database.entities

import androidx.room.Entity

@Entity(tableName = "measures", primaryKeys = ["recipeID", "ingredientID"])
data class MeasureEntity(
    val recipeID: Int,
    val ingredientID: Int,
    val measure: String
)
