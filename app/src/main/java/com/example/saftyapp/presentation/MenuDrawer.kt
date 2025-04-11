package com.example.saftyapp.presentation

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.saftyapp.navigation.Screens

@Composable
fun MenuDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToRecipes: () -> Unit = {}, // TODO (default entfernen und über args geben)
    navigateToArchive: () -> Unit = {}, // TODO (default entfernen und über args geben)
    navigateToTestSafty: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier,
) {
    ModalDrawerSheet(
        modifier = modifier,
    ) {
        SaftyAppLogo(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        )
        NavigationDrawerItem(
            label = { Text("Home") },
            icon = { Icon(Icons.Filled.Home, "Home") },
            selected = currentRoute == Screens.HomeScreen.route,
            onClick = { navigateToHome(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Recipes") },
            icon = { Icon(Icons.Filled.List, "Recipes") },
            selected = false, // TODO (Bsp: currentRoute == JetnewsDestinations.INTERESTS_ROUTE)
            onClick = { Log.i("Button", "Nav 2 is clicked"); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Archive") },
            icon = { Icon(Icons.Filled.FavoriteBorder, "Archive") },
            selected = false, // TODO (Bsp: currentRoute == JetnewsDestinations.INTERESTS_ROUTE)
            onClick = { Log.i("Button", "Nav 3 is clicked"); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        /** Button um zum Testscreen für Safty Entwicklung zu kommen -> später löschen */
        NavigationDrawerItem(
            label = { Text("Test Safty") },
            icon = { Icon(Icons.Filled.Warning, "TestSafty") },
            selected = currentRoute == Screens.TestSafty.route,
            onClick = { navigateToTestSafty(); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun SaftyAppLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(imageVector = Icons.Outlined.Face, contentDescription = "Safty") // TODO Placeholder ersetzen
        /*Icon(
            painterResource(R.drawable.ic_jetnews_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )*/
        Spacer(Modifier.width(8.dp))
        Text("Safty App") // TODO Placeholder ersetzen
        /*Icon(
            painter = painterResource(R.drawable.ic_jetnews_wordmark),
            contentDescription = stringResource(R.string.app_name),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )*/
    }
}