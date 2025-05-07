package com.example.saftyapp.model.objects

import androidx.compose.ui.graphics.Color

data class Recipe(
    val name: String,
    val instructions: String,
    val thumbnail: String? = null,
    val isCustom: Boolean,
    val isAlcoholic: Boolean,
    val keyIngredients: List<Ingredient>,
    val color: Color?,
    val allIngredients: List<String>
)