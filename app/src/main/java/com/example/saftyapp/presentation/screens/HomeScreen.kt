package com.example.saftyapp.presentation.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.viewmodels.RecipeViewModel
import com.example.saftyapp.model.viewmodels.SaftyViewModel
import com.example.saftyapp.presentation.safty.RecipeSuggestionDialog
import com.example.saftyapp.presentation.safty.Safty
import com.example.saftyapp.presentation.safty.SpeechBubble
import com.example.saftyapp.presentation.uicomponents.drawVerticalScrollbar
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: SaftyViewModel = viewModel(),
    recipeViewModel: RecipeViewModel = viewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val ingredients by recipeViewModel.ingredients.collectAsState()
    val selectedIngredients = recipeViewModel.selectedIngredients
    val scrollState = rememberLazyGridState()

    val currentExpression by viewModel.currentExpression.collectAsState()
    val fillTarget = viewModel.fillTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val liquidColor by viewModel.liquidColor.collectAsState()
    val currentWords by viewModel.currentWords.collectAsState()

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
            viewModel.cancelDrinkFinished()
        },
        onDismiss = {
            showDialog = false
            viewModel.cancelDrinkFinished()
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
                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                    fillAmount = fillAmount.value,
                    fillColor = liquidColor,
                    currentText = currentWords
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    if (!selectedIngredients.isEmpty()) {
                        coroutineScope.launch {
                            viewModel.drinkFinished()
                            delay(500L)
                            showDialog = true
                        }
                    }
                }
            ) {
                Text("Mix it!")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Ingredients", style = MaterialTheme.typography.titleMedium)

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
                    contentPadding = PaddingValues(5.dp),
                ) {
                    items(ingredients) { ingredient ->
                        val isSelected = ingredient in selectedIngredients

                        if(ingredient.isUnlocked) {
                            IngredientItem(
                                ingredient = ingredient,
                                isSelected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        recipeViewModel.deselectIngredient(ingredient)
                                        viewModel.removeIngredient(Color(255, 152, 0, 255), true)
                                    } else {
                                        recipeViewModel.selectIngredient(ingredient)
                                        viewModel.addIngredient(Color(255, 152, 0, 255), true)
                                        val comments = listOf(
                                            "Too \uD83C\uDF36 to \uFE0F\uD83D\uDC14",
                                            "Now that is rustikal\uD83D\uDE0F",
                                            "Very spaghetti carbonara"
                                        )
                                        val randomComment = comments[Random.nextInt(comments.size)]
                                        viewModel.saftySpeaketh(randomComment)
                                    }
                                    Log.i(
                                        "Ingredients",
                                        "Selected ingredients: ${selectedIngredients.joinToString()}"
                                    )
                                }
                            )
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
    ingredient: IngredientEntity,
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
        AsyncImage(
            model = ingredient.iconFilePath,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )

        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    SaftyAppTheme {
        HomeScreen(
            modifier = Modifier,
        ){

        }
    }
}