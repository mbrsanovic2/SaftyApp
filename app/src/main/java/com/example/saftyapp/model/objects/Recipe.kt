package com.example.saftyapp.model.objects

import androidx.compose.ui.graphics.Color

data class Recipe(
    val name: String,
    val instructions: String,
    val keyIngredients: List<Ingredient>,
    val allIngredients: List<String>,
    val thumbnail: String? = null,
    val isCustom: Boolean = false,
    val isAlcoholic: Boolean = false,
    val color: Color? = null
)