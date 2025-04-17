package com.example.saftyapp.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")
    object RecipeScreen : Screens(route = "recipe_screen")
    object ArchiveScreen : Screens(route = "archive_screen")
    object TestRecipeScreen : Screens("view_recipe_screen/{recipeId}") {
        fun createRoute(recipeId: String): String = "recipe_screen/$recipeId" // Maybe id int in future
    }
}