package com.example.saftyapp.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")
    object RecipeScreen : Screens(route = "recipe_screen")
    object ArchiveScreen : Screens(route = "archive_screen")
    object TestSafty : Screens(route = "test_safty_screen")
}