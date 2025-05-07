package com.example.saftyapp.presentation.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.saftyapp.R
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.viewmodels.RecipeViewModel
import com.example.saftyapp.model.viewmodels.SaftyViewModel
import com.example.saftyapp.presentation.safty.RecipeSuggestionDialog
import com.example.saftyapp.presentation.safty.Safty
import com.example.saftyapp.presentation.uicomponents.drawVerticalScrollbar
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    saftyViewModel: SaftyViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val ingredients by recipeViewModel.unlockedIngredients.collectAsState()
    val selectedIngredients = recipeViewModel.selectedIngredients
    val scrollState = rememberLazyGridState()

    val currentExpression by saftyViewModel.currentExpression.collectAsState()
    val saftyGone by saftyViewModel.saftyGone.collectAsState()
    val fillTarget = saftyViewModel.fillTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val liquidColor by saftyViewModel.liquidColor.collectAsState()
    val currentWords by saftyViewModel.currentWords.collectAsState()

    val fillAmount = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(fillTarget.value) {
        fillAmount.animateTo(fillTarget.value, tween(600))
    }

    RecipeSuggestionDialog(
        showDialog = showDialog,
        recipes = listOf("Apple Berry Smoothie", "Iced Coffee"),
        onSelect = { selectedItem ->
            onNavigateToRecipeScreen(selectedItem)
            showDialog = false
            saftyViewModel.cancelDrinkFinished()
        },
        onDismiss = {
            showDialog = false
            saftyViewModel.cancelDrinkFinished()
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val maxHeight = this.maxHeight

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 0.5f),
                contentAlignment = Alignment.Center
            ) {
                Safty(
                    expression = currentExpression,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    fillAmount = fillAmount.value,
                    fillColor = liquidColor,
                    currentText = currentWords,
                    saftyGone = saftyGone
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    if (selectedIngredients.isNotEmpty()) {
                        coroutineScope.launch {
                            saftyViewModel.drinkFinished()
                            delay(500L)
                            showDialog = true
                        }
                    }
                }
            ) {
                Text("Mix it!")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ingredients", style = MaterialTheme.typography.titleMedium)

                IconButton(
                    onClick = {
                        recipeViewModel.deselectAllIngredients()
                        saftyViewModel.clearAllIngredients()
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

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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

                        IngredientItem(
                            ingredient = ingredient,
                            isSelected = isSelected
                        ) {
                            Log.i("Path", "Path: ${ingredient.iconFilePath}")
                            if (isSelected) {
                                recipeViewModel.deselectIngredient(ingredient)
                                saftyViewModel.removeIngredient(ingredient)
                                saftyViewModel.saftySpeaketh("")
                            } else {
                                val saftyAccepted = saftyViewModel.addIngredient(ingredient)
                                if(saftyAccepted) {
                                    recipeViewModel.selectIngredient(ingredient)
                                }
                            }
//                                Log.i(
//                                    "Ingredients",
//                                    "Selected ingredients: ${selectedIngredients.joinToString { it.name }}"
//                                )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
            .padding(top = 10.dp, bottom = 10.dp)
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
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ingredient_default),
                contentDescription = "Default Ingredient Icon",
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SaftyAppTheme {
        HomeScreen(
        ) {
        }
    }
}