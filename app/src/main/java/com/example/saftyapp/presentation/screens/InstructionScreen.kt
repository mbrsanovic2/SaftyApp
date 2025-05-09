package com.example.saftyapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.saftyapp.R
import com.example.saftyapp.model.Deprecated_Recipe
import com.example.saftyapp.model.getTestRecipes
import com.example.saftyapp.model.objects.Recipe
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InstructionCard(
    recipe: Recipe,
    from: String?,
    navigateToCamera: () -> Unit,
    onFinishClicked: (Recipe) -> Unit
) {
    val backgroundColor = remember{ recipe.color }
    val image = remember{ recipe.thumbnail }

    // Launch camera upon permission granted
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var requestedPermission by remember { mutableStateOf(false) }
    
    LaunchedEffect(cameraPermissionState.status, requestedPermission) {
        if (requestedPermission && cameraPermissionState.status is PermissionStatus.Granted) {
            navigateToCamera()
            requestedPermission = false // Reset Flag
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor ?: MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = recipe.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = recipe.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Image(
                    painterResource(R.drawable.saftyapp_logo2_free),
                    contentDescription = "Default Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            RecipeDetails(recipe)

            if(from == "Safty"){
                FinishedButton(onFinishClicked = { onFinishClicked(recipe) })

                // TODO schönen button einfügen :D
                Button(onClick = {
                    if (cameraPermissionState.status is PermissionStatus.Granted) {
                        navigateToCamera()
                    } else {
                        requestedPermission = true
                        cameraPermissionState.launchPermissionRequest()
                    }
                }) {
                    Text("Take Photo")
                }
            }
        }
    }
}

@Composable
fun RecipeDetails(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        recipe.allIngredients.forEach { ingredient ->
            Text(
                text = "• $ingredient",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Preparation",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = recipe.instructions,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun FinishedButton(onFinishClicked: () -> Unit) {
    var isClicked by remember { mutableStateOf(false) }
    Button(
        onClick = {
            isClicked = true
            onFinishClicked() },
        enabled = !isClicked,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevatedButtonElevation()
    ) {
        if(!isClicked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Finish",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Finish Recipe")
        }
        else{
            Text("Completed")
        }
    }
}