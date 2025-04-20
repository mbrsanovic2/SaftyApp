package com.example.saftyapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.saftyapp.R
import com.example.saftyapp.model.Recipe
import com.example.saftyapp.model.getTestRecipes
import com.example.saftyapp.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun InstructionCard(modifier: Modifier, recipeId: String) {
    println(recipeId)
    val exampleRecipe: Recipe
    val exampleColor: Color
    val image: Int
    if(recipeId == "Apple Berry Smoothie"){
        exampleRecipe = getTestRecipes()[0]
        exampleColor = Color(228, 220, 233)
        image = R.drawable.smoothie_example_image
    }else{
        exampleRecipe = getTestRecipes()[2]
        exampleColor = MaterialTheme.colorScheme.surface
        image = R.drawable.example_drink_2
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = exampleColor
        )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = exampleRecipe.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = image),
                contentDescription = "Example Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))
            RecipeDetails(exampleRecipe)
        }
    }
}

@Composable
fun RecipeDetails(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Ingredients Header
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Ingredients List
        recipe.ingredients.forEachIndexed { index, ingredient ->
            val measure = recipe.measures.getOrNull(index) ?: ""
            Text(
                text = "• $ingredient: $measure",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preparation Header
        Text(
            text = "Preparation",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Preparation Text
        Text(
            text = recipe.instructions,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview
@Composable
fun Preview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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
                if (!hideBottomBar) {
                    BottomBarXP()
                }
            }
        ) { innerPadding ->
            var modifier = Modifier.padding(innerPadding)
            InstructionCard(modifier = modifier, "Spaghetti al la carbon")
        }
    }
}