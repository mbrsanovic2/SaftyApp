package com.example.saftyapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.saftyapp.model.viewmodels.PhotoViewModel

@Composable
fun ArchiveScreen(
    modifier: Modifier,
    viewModel: PhotoViewModel
) {
    val photos = viewModel.photoUris

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Archive Screen -> Zeigt testweise die gemachten Fotos an :)"
        )

        LazyColumn {
            items(photos) { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(200.dp)
                )
            }
        }
    }
}