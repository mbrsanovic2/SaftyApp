package com.example.saftyapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.saftyapp.presentation.ArchiveScreen
import com.example.saftyapp.presentation.BottomBarXP
import com.example.saftyapp.presentation.HomeScreen
import com.example.saftyapp.presentation.MenuDrawer
import com.example.saftyapp.presentation.RecipeScreen
import com.example.saftyapp.presentation.TestRecipeScreen
import com.example.saftyapp.presentation.TopBar
import kotlinx.coroutines.launch

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Make current route observable
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: Screens.HomeScreen.route

    val hideBottomBar = when (currentRoute) {
        Screens.ArchiveScreen.route -> true
        else -> false
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                currentRoute = currentRoute,
                navigateToHome = { navController.navigate(Screens.HomeScreen.route) },
                navigateToRecipes = { navController.navigate(Screens.RecipeScreen.route) },
                navigateToArchive = { navController.navigate(Screens.ArchiveScreen.route) },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                modifier = Modifier
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            },
            bottomBar = {
                if(!hideBottomBar) {
                    BottomBarXP()
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.HomeScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screens.HomeScreen.route) {
                    HomeScreen(
                        modifier = Modifier,
                        onNavigateToRecipeScreen = { recipe ->
                            navController.navigate(Screens.TestRecipeScreen.createRoute(recipe))
                        }
                    )
                }
                composable(route = Screens.RecipeScreen.route) {
                    RecipeScreen(modifier = Modifier)
                }
                composable(route = Screens.ArchiveScreen.route) {
                    ArchiveScreen(modifier = Modifier)
                }
                composable(
                    route = "recipe_screen/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.StringType
                    })
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "No Recipe"
                    TestRecipeScreen(modifier = Modifier, recipeId = recipeId)
                }
            }
        }
    }
}
