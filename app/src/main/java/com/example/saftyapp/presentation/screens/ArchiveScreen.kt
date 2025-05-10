package com.example.saftyapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import com.example.saftyapp.model.getTestArchive
import com.example.saftyapp.model.viewmodels.ArchiveViewModel
import com.example.saftyapp.model.viewmodels.PhotoViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ArchiveScreen(
    photoViewModel: PhotoViewModel,
    archiveViewModel: ArchiveViewModel = hiltViewModel(),
    onNavigateToRecipeScreen: (String) -> Unit
) {
    val photos = photoViewModel.photoUris
    val archiveEntries by archiveViewModel.archiveEntries.collectAsState()
//    val archiveEntries = getTestArchive()
    val grouped = archiveEntries
        .sortedByDescending { it.date }
        .groupBy { entry ->
            val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            formatter.format(entry.date)
        }

//        LazyColumn {
//            items(photos) { uri ->
//                Image(
//                    painter = rememberImagePainter(uri),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .height(200.dp)
//                )
//            }
//        }

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
                            .heightIn(min = 0.dp, max = 1000.dp), // Damit Grid nicht "unendlich" wächst
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            end = 2.dp,
                            top = 2.dp,
                            bottom = 20.dp
                        )
                    ) {
                        items(recipesInMonth) { archiveEntry ->
                            RecipeCard(
                                name = archiveEntry.recipe.name,
                                image = archiveEntry.recipe.thumbnail,
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


