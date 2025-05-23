package com.example.saftyapp.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.model.viewmodels.ArchiveViewModel
import com.example.saftyapp.model.viewmodels.PhotoViewModel
import com.example.saftyapp.model.viewmodels.RecipeViewModel
import com.example.saftyapp.model.viewmodels.XPViewModel
import com.example.saftyapp.presentation.screens.ArchiveScreen
import com.example.saftyapp.presentation.screens.CameraScreen
import com.example.saftyapp.presentation.uicomponents.BottomBarXP
import com.example.saftyapp.presentation.screens.HomeScreen
import com.example.saftyapp.presentation.uicomponents.MenuDrawer
import com.example.saftyapp.presentation.screens.RecipeScreen
import com.example.saftyapp.presentation.screens.InstructionScreen
import com.example.saftyapp.presentation.screens.RecipeForm
import com.example.saftyapp.presentation.uicomponents.AdvancedJuicyMessage
import com.example.saftyapp.presentation.uicomponents.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val photoViewModel: PhotoViewModel = viewModel()
    val xpViewModel: XPViewModel = hiltViewModel()
    val recipeViewModel: RecipeViewModel = hiltViewModel()
    val archiveViewmodel: ArchiveViewModel = hiltViewModel()

    val selectedRecipe = recipeViewModel.selectedRecipe.collectAsState()
    val userState = xpViewModel.userState.collectAsState()
    var tempXPBar by remember{ mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    var showJuicyDialog by remember { mutableStateOf(false) }
    val currentRoute = navBackStackEntry.value?.destination?.route ?: Screens.HomeScreen.route

    val hideBottomBar = when (currentRoute) {
        Screens.HomeScreen.route -> false
        else -> true
    }

    val gainXP: (Int) -> Unit = { amount ->
        coroutineScope.launch {
            tempXPBar = true
            xpViewModel.gainXP(amount)
            delay(amount * 200L + 1500L)
            tempXPBar = false
        }
    }

    // Message when becoming advanced Juicy Maker
    AdvancedJuicyMessage(
        showJuicyDialog,
        "Congrats you are now an advanced Juicy Maker, you have unlocked all ingredients and are now able to create your own recipes"
    ) { showJuicyDialog = false }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                currentRoute = currentRoute,
                navigateToHome = { navController.navigate(Screens.HomeScreen.route) },
                navigateToRecipes = { navController.navigate(Screens.RecipeScreen.route) },
                navigateToArchive = { navController.navigate(Screens.ArchiveScreen.route) },
                navigateToRecipeCreator = { navController.navigate(Screens.RecipeCreationScreen.route) },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                advancedJuicy = userState.value.isJUICY,
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
                // Bar now slides when XP Gain :)
                AnimatedVisibility(
                    visible = !hideBottomBar || tempXPBar,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    BottomBarXP(
                        viewModel = xpViewModel,
                        onLevelUp = { levelUpData ->
                            showJuicyDialog = levelUpData.unlockedJuicy
                            recipeViewModel.updateRecentlyUnlocked(levelUpData.unlockedIngredients)
                        }
                    )
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
                        recipeViewModel = recipeViewModel,
                        onNavigateToRecipeScreen = { recipe ->
                            recipeViewModel.setSelectedRecipe(recipe)
                            navController.navigate(
                                Screens.InstructionScreen.createRoute(
                                    "Safty"
                                )
                            )
                        }
                    )
                }

                // RecipeScreen
                composable(route = Screens.RecipeScreen.route) {
                    RecipeScreen(
                        recipeViewModel = recipeViewModel,
                        onNavigateToRecipeScreen = { recipe ->
                            recipeViewModel.setSelectedRecipe(recipe)
                            navController.navigate(Screens.InstructionScreen.createRoute())
                        })
                }

                // ArchiveScreen
                composable(route = Screens.ArchiveScreen.route) {
                    ArchiveScreen(
                        archiveViewModel = archiveViewmodel,
                        onNavigateToRecipeScreen = { recipe ->
                            recipeViewModel.setSelectedRecipe(recipe)
                            navController.navigate(Screens.InstructionScreen.createRoute("Archive"))
                        }
                    )
                }

                // InstructionScreen
                composable(
                    route = Screens.InstructionScreen.route,
                    arguments = listOf(
                        navArgument("from") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                        navArgument("recipeName") {
                            type = NavType.StringType
                            nullable = true
                        }
                    )
                ) { backStackEntry ->
                    val from = backStackEntry.arguments?.getString("from")
                    val fallbackRecipe =  Recipe(
                        name = "NONE",
                        instructions = "This Recipe does not actually exist :(",
                    )
                    val alreadyScored = selectedRecipe.value?.hasBeenScored

                    InstructionScreen(
                        recipe = selectedRecipe.value ?: fallbackRecipe,
                        from = from,
                        navigateToCamera = {
                            val recipeName = (selectedRecipe.value ?: fallbackRecipe).name
                            navController.navigate(Screens.CameraScreen.createRoute(recipeName))
                        },
                        alreadyScored = alreadyScored ?: false,
                        onFinishClicked = { recipe ->
                            gainXP(5)
                            coroutineScope.launch {
                                recipeViewModel.scoreRecipe(recipe.name)
                                archiveViewmodel.addEntry(recipe)
                            }
                        }
                    )
                }

                // Camera Screen
                composable(
                    route = Screens.CameraScreen.route,
                    arguments = listOf(
                        navArgument("recipeName") {
                            type = NavType.StringType
                            nullable = true
                        }
                    )
                ) { backStackEntry ->
                    val recipeNameFromNav = backStackEntry.arguments?.getString("recipeName") ?: ""

                    CameraScreen(
                        viewModel = photoViewModel,
                        recipeName = recipeNameFromNav,
                        onPhotoTaken = {
                            navController.navigate(Screens.InstructionScreen.createRoute("Archive"))
                        },
                        onDrinkDetected = { recipeName ->
                            gainXP(10)
                            coroutineScope.launch {
                                photoViewModel.savePhotoInDB(recipeName)
                                archiveViewmodel.loadEntries()
                            }
                        }
                    )
                }

                // Recipe Creation Screen
                composable(route = Screens.RecipeCreationScreen.route) {
                    RecipeForm { recipeName, ingredients, preparation ->
                        coroutineScope.launch {
                            recipeViewModel.addRecipe(
                                recipeName = recipeName,
                                ingredients = ingredients,
                                preparation = preparation
                            )
                            //print("Rezept wurde wirklich erstellt")
                        }
                        navController.navigate(Screens.HomeScreen.route)
                    }
                }
            }
        }
    }
}
