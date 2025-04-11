package com.example.saftyapp.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R
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
        SaftyDrawerItem(
            label = "Safty's Home",
            icon = {
                Image(
                    painter = painterResource(R.drawable.safty_icon),
                    contentDescription = "Safty's Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            selected = currentRoute == Screens.HomeScreen.route,
            onClick = { navigateToHome(); closeDrawer() },
        )
        SaftyDrawerItem(
            label = "Recipes",
            icon = { Icon(Icons.Filled.List, "Recipes") },
            selected = false, // TODO (Bsp: currentRoute == JetnewsDestinations.INTERESTS_ROUTE)
            onClick = { Log.i("Button", "Nav 2 is clicked"); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
        )
        SaftyDrawerItem(
            label = "Archive",
            icon = { Icon(Icons.Filled.FavoriteBorder, "Archive") },
            selected = false, // TODO (Bsp: currentRoute == JetnewsDestinations.INTERESTS_ROUTE)
            onClick = { Log.i("Button", "Nav 3 is clicked"); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
        )
        /** Button um zum Testscreen für Safty Entwicklung zu kommen -> später löschen */
        SaftyDrawerItem(
            label = "Test Safty",
            icon = { Icon(Icons.Filled.Warning, "TestSafty") },
            selected = currentRoute == Screens.TestSafty.route,
            onClick = { navigateToTestSafty(); closeDrawer()  }, // TODO (Bsp: navigateToInterests)
        )
    }
}

@Composable
private fun SaftyAppLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painterResource(R.drawable.saftyapp_logo2_free),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.width(10.dp))
        Image(
            painter = painterResource(R.drawable.saftyapp_logo_text),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun SaftyDrawerItem(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = icon,
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}