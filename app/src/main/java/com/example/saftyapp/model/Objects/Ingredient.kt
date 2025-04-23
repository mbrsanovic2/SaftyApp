package com.example.saftyapp.model.Objects

import androidx.compose.ui.graphics.Color

data class Ingredient(
    val name: String,
    val iconFilePath: String? = null,
    val color: Color,
    val isUnlocked: Boolean,
    val measure: String? = null
)
