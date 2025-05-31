package com.example.saftyapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun RecipeForm(
    onSaveRecipe: (String, List<String>, String, Color) -> Unit
) {
    val primaryColor = colorScheme.surface
    var recipeName by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf("")) }
    var preparation by remember { mutableStateOf("") }
    var showColorPickerDialog by remember { mutableStateOf(false) }

    var containerColor by remember { mutableStateOf(primaryColor) }
    var showError by remember { mutableStateOf(false) }

    if (showColorPickerDialog) {
        ColorPickerDialog(
            initialColor = primaryColor,
            onColorSelected = { newColor ->
                val adjustedColor = newColor.copy(alpha = 0.4f)
                containerColor = adjustedColor
            },
            onDismissRequest = { showColorPickerDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create a Recipe",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = recipeName,
                onValueChange = { recipeName = it },
                label = { Text("Recipe Name") },
                modifier = Modifier.fillMaxWidth()
            )
            if (showError && recipeName.isBlank()) {
                Text(
                    text = "Preparation steps are required",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ingredients",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            ingredients.forEachIndexed { index, ingredient ->
                OutlinedTextField(
                    value = ingredient,
                    onValueChange = { newValue ->
                        ingredients = ingredients.toMutableList().apply { this[index] = newValue }
                    },
                    label = { Text("Ingredient") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            if (showError && ingredients.getOrNull(0)?.isBlank() != false) {
                Text(
                    text = "At least 1 ingredient is required",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextButton(onClick = {
                ingredients = ingredients + ""
            }) {
                Text("+ Add ingredient")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Preparation",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = preparation,
                onValueChange = { preparation = it },
                label = { Text("Preparation") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )
            if (showError && preparation.isBlank()) {
                Text(
                    text = "Preparation steps are required",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(modifier = Modifier.fillMaxWidth(), onClick = { showColorPickerDialog = true }) {
                Text("Pick Color")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val currentIngredients = ingredients.filter { it.isNotBlank() }
                    if (recipeName.isNotBlank() && preparation.isNotBlank() && currentIngredients.isNotEmpty()) {
                        onSaveRecipe(
                            recipeName,
                            ingredients.filter { it.isNotBlank() },
                            preparation,
                            containerColor
                        )
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(255, 102, 51))
            ) {
                Text("SAVE RECIPE")
            }
        }
    }
}

@Composable
fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismissRequest: () -> Unit
) {
    val controller = rememberColorPickerController()
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Card Color") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    controller = controller,
                    drawOnPosSelected = {
                        drawColorIndicator(
                            controller.selectedPoint.value,
                            controller.selectedColor.value,
                        )
                    },
                    onColorChanged = { colorEnvelope ->
                        onColorSelected(colorEnvelope.color)
                    },
                    initialColor = initialColor
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Done")
            }
        }
    )
}