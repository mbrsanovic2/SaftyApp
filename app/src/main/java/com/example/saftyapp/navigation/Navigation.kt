package com.example.saftyapp.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.saftyapp.presentation.HomeScreen
import com.example.saftyapp.presentation.MenuDrawer
import com.example.saftyapp.presentation.TestSafty
import kotlinx.coroutines.launch

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Make current route observable
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route ?: Screens.HomeScreen.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuDrawer(
                currentRoute = currentRoute,
                navigateToHome = { navController.navigate(Screens.HomeScreen.route) },
                navigateToRecipes = { navController.navigate(Screens.RecipeScreen.route) },
                navigateToArchive = { navController.navigate(Screens.ArchiveScreen.route) },
                navigateToTestSafty = { navController.navigate(Screens.TestSafty.route) },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                modifier = Modifier
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.route,
            modifier = Modifier
        ) {
            composable(route = Screens.HomeScreen.route) {
                HomeScreen(
                    modifier = Modifier,
                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                )
            }
            composable(route = Screens.RecipeScreen.route) {
                // TODO RecipeScreen(modifier = Modifier)
            }
            composable(route = Screens.ArchiveScreen.route) {
                // TODO ArchiveScreen(modifier = Modifier)
            }
            composable(route = Screens.TestSafty.route) {
                TestSafty(
                    modifier = Modifier,
                    onOpenDrawer = { coroutineScope.launch { drawerState.open() } },
                )
            }
        }
    }
}
