package com.example.saftyapp.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isCustom: Boolean,
    val isAlcoholic: Boolean=false,
    val instructions: String,
    val thumbnail: String? = null,
)
