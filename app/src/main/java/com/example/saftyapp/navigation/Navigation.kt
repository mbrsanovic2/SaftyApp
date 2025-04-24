package com.example.saftyapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.saftyapp.model.viewmodels.PhotoViewModel
import com.example.saftyapp.presentation.screens.ArchiveScreen
import com.example.saftyapp.presentation.screens.CameraScreen
import com.example.saftyapp.presentation.uicomponents.BottomBarXP
import com.example.saftyapp.presentation.screens.HomeScreen
import com.example.saftyapp.presentation.uicomponents.MenuDrawer
import com.example.saftyapp.presentation.screens.RecipeScreen
import com.example.saftyapp.presentation.screens.InstructionCard
import com.example.saftyapp.presentation.screens.RecipeForm
import com.example.saftyapp.presentation.uicomponents.TopBar
import kotlinx.coroutines.launch

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val photoViewModel: PhotoViewModel = viewModel()

    // Make current route observable
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: Screens.HomeScreen.route

    val hideBottomBar = when {
        currentRoute == Screens.ArchiveScreen.route -> true
        currentRoute == Screens.RecipeScreen.route -> true
        currentRoute == Screens.CameraScreen.route -> true
        currentRoute == Screens.RecipeCreationScreen.route -> true
        currentRoute.startsWith("instruction_screen/") -> true
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
                navigateToRecipeCreator = {navController.navigate(Screens.RecipeCreationScreen.route)},
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
                // HomeScreen
                composable(route = Screens.HomeScreen.route) {
                    HomeScreen(
                        modifier = Modifier,
                        onNavigateToRecipeScreen = { recipe ->
                            navController.navigate(Screens.InstructionScreen.createRoute(recipe, "Safty"))
                        }
                    )
                }

                // RecipeScreen
                composable(route = Screens.RecipeScreen.route) {
                    RecipeScreen(
                        modifier = Modifier,
                        onNavigateToRecipeScreen = { recipe ->
                            navController.navigate(Screens.InstructionScreen.createRoute(recipe))
                        })
                }

                // ArchiveScreen
                composable(route = Screens.ArchiveScreen.route) {
                    ArchiveScreen(
                        modifier = Modifier,
                        viewModel = photoViewModel
                    )
                }

                // InstructionScreen
                composable(
                    route = Screens.InstructionScreen.route,
                    arguments = listOf(
                        navArgument("recipeId") {
                            type = NavType.StringType
                        },
                        navArgument("from") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "No Recipe"
                    val from = backStackEntry.arguments?.getString("from")

                    InstructionCard(
                        modifier = Modifier,
                        recipeId = recipeId,
                        from = from,
                        navigateToCamera = { navController.navigate(Screens.CameraScreen.route) }
                    )
                }

                // Camera Screen
                composable(route = Screens.CameraScreen.route) {
                    CameraScreen(
                        viewModel = photoViewModel,
                        onPhotoTaken = {
                            navController.navigate(Screens.ArchiveScreen.route)
                        }
                    )
                }

                composable(route = Screens.RecipeCreationScreen.route) {
                    RecipeForm(
                    ){ _,_,_ ->
                      print("Rezept wurde theoretisch erstellt")
                    }
                }
            }
        }
    }
}
