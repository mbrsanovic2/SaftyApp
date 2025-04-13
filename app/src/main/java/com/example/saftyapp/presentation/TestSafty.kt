package com.example.saftyapp.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R
import com.example.saftyapp.model.SaftyExpression
import com.example.saftyapp.ui.theme.SaftyAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSafty(
    modifier: Modifier,
    onOpenDrawer: () -> Unit
) {
    var currentExpression by remember { mutableStateOf(SaftyExpression.Happy) }
    val fillAmount = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
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

            Safty(currentExpression, modifier, fillAmount.value)

            Button(
                onClick = {
                    currentExpression = SaftyExpression.entries
                        .filterNot { it == currentExpression }
                        .random()
                    val target = (fillAmount.value + 0.1f).coerceAtMost(1f)
                    coroutineScope.launch {
                        fillAmount.animateTo(
                            targetValue = target,
                            animationSpec = tween(durationMillis = 600)
                        )
                    }
                }) {
                Text("Gib Safty was :)")
            }
        }
    }
}

@Composable
fun Safty(
    expression: SaftyExpression,
    modifier: Modifier = Modifier,
    fillAmount: Float = 0f
) {
    Box(
        modifier = Modifier.size(400.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fill),
            contentDescription = "Fluid",
            contentScale = ContentScale.Fit,
            modifier = modifier.drawWithContent {
                clipRect(
                    top = size.height - size.height * fillAmount,
                    bottom = size.height
                ) {
                    this@drawWithContent.drawContent()
                }
            }
        )
        Image(
            painter = painterResource(id = R.drawable.safty_default),
            contentDescription = "Safty",
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
        Image(
            painter = painterResource(id = expression.drawableRes),
            contentDescription = "Expression",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .offset(x = 3.dp)

        )
    }
}

@Preview
@Composable
fun SaftyScreen() {
    SaftyAppTheme {
        TestSafty(modifier = Modifier.fillMaxSize()) { }
    }
}