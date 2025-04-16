package com.example.saftyapp.presentation

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.saftyapp.R
import com.example.saftyapp.model.SaftyViewModel
import com.example.saftyapp.navigation.Screens
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: SaftyViewModel = viewModel(),
    onOpenDrawer: () -> Unit
) {
    val xpTotal: Int = 30 // TODO Wert von DB holen
    val xpCurrent: Int = 8 // TODO Wert von DB holen
    val xpLevel: Int = 3 // TODO Wert von DB holen
    val hasTitle: Boolean = true // TODO Wert von DB holen
    val ingredients: List<String> = listOf(
        "Lemon Juice",
        "Lime Juice",
        "Simple Syrup",
        "Mint Leaves",
        "Ginger",
        "Cucumber",
        "Soda Water",
        "Tonic Water",
        "Orange Juice",
        "Pineapple Juice",
        "Coconut Milk",
        "Honey"
    ) // TODO Liste von DB holen
    // TODO Liste mit Ingredient Images?
    val selectedItems = remember { mutableStateListOf<String>() }
    val scrollState = rememberLazyGridState()

    val currentExpression by viewModel.currentExpression.collectAsState()
    val fillTarget = viewModel.fillTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val liquidColor by viewModel.liquidColor.collectAsState()

    val fillAmount = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(fillTarget.value) {
        fillAmount.animateTo(fillTarget.value, tween(600))
    }

    RecipeSuggestionDialog(
        showDialog = showDialog,
        recipes = listOf("Tropical Chill", "Berry Blast", "Citrus Spark"),
        onSelect = {
            showDialog = false
            viewModel.cancelDrinkFinished()
        },
        onDismiss = {
            showDialog = false
            viewModel.cancelDrinkFinished()
        }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    SaftyAppLogo(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(start = 27.dp)
                    )

                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Open Drawer"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(IntrinsicSize.Min),
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("XP Level $xpLevel", style = MaterialTheme.typography.titleMedium)

                        if (hasTitle) {
                            Text(
                                "Advanced Juicy Maker",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { (xpCurrent.toFloat() / xpTotal) },
                            modifier = Modifier
                                .width(300.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                        )

                        Text(
                            "$xpCurrent / $xpTotal",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .width(60.dp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(innerPadding)
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
                    Safty(currentExpression, modifier, fillAmount.value, liquidColor)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        if(!selectedItems.isEmpty()) {
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

                Spacer(modifier = Modifier.height(16.dp))

                Text("Ingredients", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight * 0.2f)
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
                            val isSelected = ingredient in selectedItems

                            IngredientItem(
                                ingredient = ingredient,
                                isSelected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedItems.remove(ingredient)
                                        viewModel.removeIngredient(Color(255, 152, 0, 255), true)
                                    } else {
                                        selectedItems.add(ingredient)
                                        viewModel.addIngredient(Color(255, 152, 0, 255), true)
                                    }
                                    Log.i(
                                        "Ingredients",
                                        "Selected ingredients: ${selectedItems.joinToString()}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
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
        Spacer(Modifier.width(5.dp))
        Image(
            painter = painterResource(R.drawable.saftyapp_logo_text),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun IngredientItem(
    ingredient: String,
    //image: Image,
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
        Icon(
            imageVector = Icons.Outlined.ShoppingCart, // TODO images einfügen
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )

        Text(
            text = ingredient,
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
            onOpenDrawer = {  },
        )
    }
}