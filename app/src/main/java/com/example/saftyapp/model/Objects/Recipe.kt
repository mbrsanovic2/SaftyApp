package com.example.saftyapp.model.Objects

data class Recipe(
    val name: String,
    val instructions: String,
    val thumbnail: String? = null,
    val isCustom: Boolean,
    val isAlcoholic: Boolean,
    val ingredients: List<Ingredient>,
)