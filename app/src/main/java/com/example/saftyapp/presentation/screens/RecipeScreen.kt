package com.example.saftyapp.presentation.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.saftyapp.R
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.viewmodels.RecipeViewModel
import com.example.saftyapp.presentation.uicomponents.drawVerticalScrollbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    modifier: Modifier,
    recipeViewModel: RecipeViewModel = viewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val ingredients by recipeViewModel.ingredients.collectAsState()
    val selectedIngredients = recipeViewModel.selectedIngredients
    val queryText by recipeViewModel.queryText.collectAsState()
    val filteredRecipes by recipeViewModel.filteredRecipes.collectAsState()

    val scrollState = rememberLazyGridState()
    var isFilterVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = queryText,
                onValueChange = { recipeViewModel.updateQuery(it) },
                placeholder = { Text("Search Recipes...") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { isFilterVisible = !isFilterVisible },
                modifier = Modifier.height(56.dp),
            ) {
                Text(if (isFilterVisible) "Hide" else "Ingredients")
            }

            IconButton(
                onClick = {
                    recipeViewModel.clearFilter()
                },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Filter",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        AnimatedVisibility(
            visible = isFilterVisible,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(3.dp))
                .clip(RoundedCornerShape(3.dp))
        ) {
            LazyVerticalGrid(
                state = scrollState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .drawVerticalScrollbar(scrollState),
                contentPadding = PaddingValues(
                    start = 15.dp,
                    end = 5.dp,
                    top = 5.dp,
                    bottom = 5.dp
                ),
            ) {
                items(ingredients) { ingredient ->
                    val isSelected = ingredient in selectedIngredients

                    if (ingredient.isUnlocked) {
                        IngredientItem(
                            ingredient = ingredient,
                            isSelected = isSelected
                        ) {
                            if (isSelected) {
                                recipeViewModel.deselectIngredient(ingredient)
                            } else {
                                recipeViewModel.selectIngredient(ingredient)
                            }
//                            Log.i(
//                                "Ingredients",
//                                "Selected ingredients: ${selectedIngredients.joinToString { it.name }}"
//                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredRecipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No matching recipe found :|",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    end = 2.dp,
                    top = 2.dp,
                    bottom = 20.dp
                ),
            ) {
                items(filteredRecipes) { recipe ->
                    RecipeCard(
                        name = recipe.name,
                        image = recipe.thumbnail,
                        onClick = {
                            onNavigateToRecipeScreen(recipe.name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    name: String,
    image: String?,
    onClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .size(width = 240.dp, height = 160.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (image != null) {
            AsyncImage(
                model = image,
                contentDescription = name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Image(
                painterResource(R.drawable.saftyapp_logo2_free),
                contentDescription = "Default Recipe Image",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun IngredientItem(
    ingredient: Ingredient,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                } else {
                    Color.Transparent
                }
            )
    ) {
        if (ingredient.iconFilePath != null) {
            AsyncImage(
                model = ingredient.iconFilePath,
                contentDescription = ingredient.name,
                modifier = Modifier.size(30.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Default Ingredient Icon",
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 8.dp)
            )
        }

        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}