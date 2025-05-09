package com.example.saftyapp.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")

    object RecipeScreen : Screens(route = "recipe_screen")

    object ArchiveScreen : Screens(route = "archive_screen")

    object InstructionScreen : Screens(route = "instruction_screen?from={from}") {
        fun createRoute(from: String? = null): String {
            return if (from.isNullOrBlank()) {
                "instruction_screen"
            } else {
                "instruction_screen?from=$from"
            }
        }
    }

    object CameraScreen : Screens(route = "camera_screen")

    object RecipeCreationScreen : Screens(route = "creation_screen")
}