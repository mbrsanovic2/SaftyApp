package com.example.saftyapp.presentation

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R
import com.example.saftyapp.model.SaftyExpression
import com.example.saftyapp.ui.theme.SaftyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSafty(
    modifier: Modifier,
    onOpenDrawer: () -> Unit
) {
    var currentExpression by remember { mutableStateOf(SaftyExpression.Happy) }
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

            Safty(currentExpression, modifier)

            Button(onClick = {
                currentExpression = SaftyExpression.entries
                    .filterNot { it == currentExpression }
                    .random()
            }) {
                Text("Gib Safty was :)")
            }
        }
    }
}

@Composable
fun Safty(expression: SaftyExpression, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.size(400.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.safty_default),
            contentDescription = "Vase",
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
        Image(
            painter = painterResource(id = expression.drawableRes),
            contentDescription = "Overlay",
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