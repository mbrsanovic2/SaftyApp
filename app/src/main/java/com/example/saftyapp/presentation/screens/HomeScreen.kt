package com.example.saftyapp.presentation.screens

import android.content.ClipData
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.saftyapp.R
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.viewmodels.RecipeViewModel
import com.example.saftyapp.model.viewmodels.SaftyViewModel
import com.example.saftyapp.presentation.safty.RecipeSuggestionDialog
import com.example.saftyapp.presentation.safty.Safty
import com.example.saftyapp.presentation.uicomponents.drawVerticalScrollbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    saftyViewModel: SaftyViewModel = hiltViewModel(),
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val ingredients by recipeViewModel.unlockedIngredients.collectAsState()
    val selectedIngredients = saftyViewModel.addedIngredients
    val scrollState = rememberLazyGridState()

    val currentExpression by saftyViewModel.currentExpression.collectAsState()
    val saftyGone by saftyViewModel.saftyGone.collectAsState()
    val fillTarget = saftyViewModel.fillTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val liquidColor by saftyViewModel.liquidColor.collectAsState()
    val currentWords by saftyViewModel.currentWords.collectAsState()

    val fillAmount = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var recommendedDrinks = remember { mutableStateOf(emptyList<String>()) }
    val eventState = remember { mutableStateOf("") }

    SoundEventHandler(LocalContext.current, eventState.value)

    LaunchedEffect(fillTarget.value) {
        fillAmount.animateTo(fillTarget.value, tween(600))
    }

    RecipeSuggestionDialog(
        showDialog = showDialog,
        recipes = recommendedDrinks.value,
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
                { name ->
                    val ingredient = ingredients.find { it.name == name }
                    if (ingredient != null) {
                        val isSelected = ingredient in selectedIngredients
                        if (isSelected) {
                            saftyViewModel.removeIngredient(ingredient)
                            saftyViewModel.saftySpeaketh("")
                        } else {
                            saftyViewModel.addIngredient(ingredient)
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    if (selectedIngredients.isNotEmpty()) {
                        coroutineScope.launch {
                            recommendedDrinks.value = saftyViewModel.drinkFinished()
                            eventState.value = "wrrrm"
                            delay(500L)
                            showDialog = true
                        }
                    }
                }
            ) {
                Text("Mix it!")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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

            Spacer(modifier = Modifier.height(8.dp))

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
                            isSelected = isSelected,
                            recentlyUnlocked = ingredient.recentlyUnlocked
                        ) {
                            if (isSelected) {
                                saftyViewModel.removeIngredient(ingredient)
                                saftyViewModel.saftySpeaketh("")
                            } else {
                                val wasAccepted = saftyViewModel.addIngredient(ingredient)
                                if(!wasAccepted){
                                    eventState.value = "nyeeh"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IngredientItem(
    ingredient: Ingredient,
    isSelected: Boolean,
    recentlyUnlocked: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }

    val borderColor = if (recentlyUnlocked) {
        Color(0xFFFFD633)
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp)
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .drawBehind {
                    drawRoundRect(
                        color = borderColor,
                        cornerRadius = CornerRadius(8.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                .padding(vertical = 4.dp)
                .padding(start = 2.dp, end = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(28.dp)) {
                    if (ingredient.iconFilePath != null) {
                        AsyncImage(
                            model = ingredient.iconFilePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ingredient_default),
                            contentDescription = "Default Ingredient Icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Overlay for Drag & Drop
                    Box(modifier = Modifier.matchParentSize()) {
                        AsyncImage(
                            model = ingredient.iconFilePath,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = {
                                            startTransfer(
                                                DragAndDropTransferData(
                                                    ClipData.newPlainText(
                                                        "ingredient name",
                                                        ingredient.name
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }

                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            if (recentlyUnlocked) {
                Text(
                    text = "New!",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 18.dp, y = (-13).dp)
                        .background(Color(0xFFFFD633), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SoundEventHandler(context: Context, event: String) {
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(event) {
        mediaPlayer.value?.release() // Release previous player

        val soundResId = when (event) {
            //"crunch" -> R.raw.nyeeh
            "wrrrm" -> R.raw.billy
            else -> null
        }

        soundResId?.let {
            mediaPlayer.value = MediaPlayer.create(context, it).apply {
                setOnCompletionListener {
                    release()
                    mediaPlayer.value = null
                }
                start()
            }
        }
    }
}