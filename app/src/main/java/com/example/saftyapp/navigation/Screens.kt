package com.example.saftyapp.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")

    object RecipeScreen : Screens(route = "recipe_screen")

    object ArchiveScreen : Screens(route = "archive_screen")

    object InstructionScreen : Screens(route = "instruction_screen/{recipeId}?from={from}") {
        fun createRoute(recipeId: String, from: String? = null): String {
            return if (from.isNullOrBlank()) {
                "instruction_screen/$recipeId"
            } else {
                "instruction_screen/$recipeId?from=$from"
            }
        }
    }

    object CameraScreen : Screens(route = "camera_screen")
}