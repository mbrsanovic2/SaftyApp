package com.example.saftyapp.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.saftyapp.R
import com.example.saftyapp.model.SaftyExpression
import com.example.saftyapp.model.SaftyViewModel
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlin.random.Random

private var safty_size = 400.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSafty(
    modifier: Modifier,
    viewModel: SaftyViewModel = viewModel(),
    onOpenDrawer: () -> Unit,
) {
    val currentExpression by viewModel.currentExpression.collectAsState()
    val fillTarget = viewModel.fillTarget.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val liquidColor by viewModel.liquidColor.collectAsState()

    val fillAmount = remember { Animatable(0f) }
    LaunchedEffect(fillTarget.value) {
        fillAmount.animateTo(fillTarget.value, tween(600))
    }

    if (showDialog) {
        RecipeSuggestionDialog(
            recipes = listOf("Tropical Chill", "Berry Blast", "Citrus Spark"),
            onSelect = { selected ->
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Safty App") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Open Drawer"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Leb deinen Safty Traum aus :D Natürlich ^^",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            Safty(currentExpression, modifier, fillAmount.value, liquidColor)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = {
                    viewModel.addIngredient(Color(229, 56, 56, 255), true)
                }) {
                    Text("Erdbeere :)")
                }
                Button(onClick = {
                    viewModel.addIngredient(Color(51, 51, 208, 255), true)
                }) {
                    Text("Heidelbeere :)")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(onClick = {
                    viewModel.addIngredient(Color(255, 224, 0), true)
                }) {
                    Text("Banane :)")
                }
                Button(onClick = {
                    viewModel.addIngredient(Color(241, 162, 9, 255), false)
                }) {
                    Text("Carbonara :(")
                }
            }
            Button(onClick = {
                showDialog = true
            }) {
                Text("Mixen")
            }
        }
    }
}

@Composable
fun Safty(
    expression: SaftyExpression,
    modifier: Modifier = Modifier,
    fillAmount: Float = 0f,
    fillColor: Color
) {
    Box(
        modifier = Modifier.size(safty_size)
    ) {
        SaftyImage(
            image = R.drawable.fill,
            contentDescription = "Fluid",
            colorFilter = ColorFilter.tint(fillColor),
            modifier = modifier.drawWithContent {
                clipRect(
                    top = size.height - size.height * fillAmount,
                    bottom = size.height
                ) {
                    this@drawWithContent.drawContent()
                }
            }
        )
        SaftyImage(
            image = R.drawable.safty_default,
            contentDescription = "Safty",
            modifier = modifier
        )

        SaftyImage(
            image = expression.drawableRes,
            contentDescription = "Expression",
            modifier = modifier
                .offset(x = 3.dp)

        )
    }
}
@Composable
fun SaftyImage(
    image: Int,
    contentDescription: String,
    modifier: Modifier,
    colorFilter: ColorFilter? = null
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = contentDescription,
        colorFilter = colorFilter,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

@Composable
fun RecipeSuggestionDialog(
    recipes: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Choose a Recipe", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                recipes.forEach { recipe ->
                    Button(
                        onClick = { onSelect(recipe) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text(recipe)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
fun SaftyScreen() {
    SaftyAppTheme {
        TestSafty(modifier = Modifier.fillMaxSize()) { }
    }
}