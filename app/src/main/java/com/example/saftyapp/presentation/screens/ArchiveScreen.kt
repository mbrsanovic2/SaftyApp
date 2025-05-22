package com.example.saftyapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.saftyapp.model.viewmodels.ArchiveViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ArchiveScreen(
    archiveViewModel: ArchiveViewModel = hiltViewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val archiveEntries by archiveViewModel.archiveEntries.collectAsState()
    val grouped = archiveEntries
        .sortedByDescending { it.date }
        .groupBy { entry ->
            val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            formatter.format(entry.date)
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (archiveEntries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mix some drinks with Safty and finish the recipes to add them to your archive :)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            grouped.forEach { (monthYear, recipesInMonth) ->
                item {
                    Text(
                        text = monthYear,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 0.dp, max = 1000.dp),
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            end = 2.dp,
                            top = 2.dp,
                            bottom = 20.dp
                        )
                    ) {
                        items(recipesInMonth) { archiveEntry ->
                            val shownImage = if (archiveEntry.imageFilePath?.let { File(it).exists() } == true) {
                                archiveEntry.imageFilePath
                            } else {
                                archiveEntry.recipe.thumbnail
                            }

                            RecipeCard(
                                name = archiveEntry.recipe.name,
                                image = shownImage,
                                onClick = {
                                    onNavigateToRecipeScreen(archiveEntry.recipe.name)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


