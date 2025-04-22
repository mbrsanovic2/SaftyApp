package com.example.saftyapp.presentation.safty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.saftyapp.R
import com.example.saftyapp.model.SaftyExpression

@Composable
fun Safty(
    expression: SaftyExpression,
    modifier: Modifier = Modifier,
    fillAmount: Float = 0f,
    fillColor: Color
) {
    Box(
        modifier = modifier
    ) {
        SaftyImage(
            image = R.drawable.safty_default,
            contentDescription = "Safty",
            modifier = modifier
        )

        SaftyImage(
            image = R.drawable.fill,
            contentDescription = "Fluid",
            colorFilter = ColorFilter.tint(fillColor, blendMode = BlendMode.Modulate),
            modifier = modifier
                .alpha(0.85f)
                .drawWithContent {
                    clipRect(
                        top = size.height - size.height * fillAmount,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                }
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
    showDialog: Boolean,
    recipes: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if(showDialog) {
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
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
}