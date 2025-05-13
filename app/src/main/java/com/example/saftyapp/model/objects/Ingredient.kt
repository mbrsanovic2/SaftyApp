package com.example.saftyapp.model.objects

import androidx.compose.ui.graphics.Color

data class Ingredient(
    val name: String,
    val iconFilePath: String? = null,
    val color: Color = Color.Transparent,
    val isUnlocked: Boolean = false,
    val recentlyUnlocked: Boolean = false
)
