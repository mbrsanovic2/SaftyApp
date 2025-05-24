package com.example.saftyapp.model.objects

import androidx.compose.ui.graphics.Color

data class Recipe(
    val name: String,
    val instructions: String = "",
    val keyIngredients: List<Ingredient> = emptyList(),
    val allIngredients: List<String> = emptyList(),
    val thumbnail: String? = null,
    val isCustom: Boolean = false,
    val isAlcoholic: Boolean = false,
    val color: Color? = null,
    val hasBeenScored: Boolean = false,
    val hasBeenPhotoScored: Boolean = false
)