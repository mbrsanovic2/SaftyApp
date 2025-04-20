package com.example.saftyapp.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")
    object RecipeScreen : Screens(route = "recipe_screen")
    object ArchiveScreen : Screens(route = "archive_screen")
    object InstructionScreen : Screens(route = "instruction_screen/{recipeId}") {
        fun createRoute(recipeId: String): String = "instruction_screen/$recipeId" // Maybe id int in future
    }
}