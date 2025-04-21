package com.example.saftyapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.saftyapp.R
import com.example.saftyapp.model.Recipe
import com.example.saftyapp.model.getTestRecipes

@Composable
fun InstructionCard(modifier: Modifier, recipeId: String, from: String?) {
    val exampleRecipe: Recipe
    val exampleColor: Color
    val image: Int
    if(recipeId == "Apple Berry Smoothie"){
        exampleRecipe = getTestRecipes()[0]
        exampleColor = Color(228, 220, 233)
        image = R.drawable.smoothie_example_image
    }else{
        exampleRecipe = getTestRecipes()[2]
        exampleColor = MaterialTheme.colorScheme.surface
        image = R.drawable.example_drink_2
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = exampleColor
        )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = exampleRecipe.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = image),
                contentDescription = "Example Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))
            RecipeDetails(exampleRecipe)
            if(from == "Safty"){
                FinishedButton()
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

        recipe.ingredients.forEachIndexed { index, ingredient ->
            val measure = recipe.measures.getOrNull(index) ?: ""
            Text(
                text = "• $ingredient: $measure",
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
fun FinishedButton() {
    var isClicked by remember { mutableStateOf(false) }

    val backgroundColor = MaterialTheme.colorScheme.primary
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Button(
        onClick = { isClicked = true },
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

fun Color.darken(factor: Float): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}