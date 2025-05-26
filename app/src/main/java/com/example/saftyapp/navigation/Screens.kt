package com.example.saftyapp.navigation

import android.net.Uri

sealed class Screens(val route: String) {
    object HomeScreen : Screens(route = "home_screen")

    object RecipeScreen : Screens(route = "recipe_screen")

    object ArchiveScreen : Screens(route = "archive_screen")

    object InstructionScreen : Screens(route = "instruction_screen?from={from}") {
        fun createRoute(from: String? = null, recipeName: String? = null): String {
            val fromParam = from ?: "camera"
            val recipeParam = recipeName ?: "NONE"
            return "instruction_screen?from=$fromParam&recipeName=$recipeParam"
        }
    }

    object CameraScreen : Screens(route = "camera_screen?recipeName={recipeName}") {
        fun createRoute(recipeName: String): String {
            return "camera_screen?recipeName=${Uri.encode(recipeName)}"
        }
    }

    object RecipeCreationScreen : Screens(route = "creation_screen")

    object LoadingScreen : Screens(route = "loading_screen")
}