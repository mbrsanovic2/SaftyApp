package com.example.saftyapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
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
    navigateToRecipes: () -> Unit,
    navigateToArchive: () -> Unit,
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
            selected = currentRoute == Screens.RecipeScreen.route,
            onClick = { navigateToRecipes(); closeDrawer() },
        )
        SaftyDrawerItem(
            label = "Archive",
            icon = { Icon(Icons.Filled.FavoriteBorder, "Archive") },
            selected = currentRoute == Screens.ArchiveScreen.route,
            onClick = { navigateToArchive(); closeDrawer() },
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