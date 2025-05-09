package com.example.saftyapp.model.database.entities

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey
    val name: String,
    val iconFilePath: String? = null,
    var color: Color,
    val isUnlocked: Boolean = false
)
