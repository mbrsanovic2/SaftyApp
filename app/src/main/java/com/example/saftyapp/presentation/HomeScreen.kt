package com.example.saftyapp.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    onOpenDrawer: () -> Unit
) {
    var xpTotal: Int = 30 // TODO Wert von DB holen
    var xpCurrent: Int = 8 // TODO Wert von DB holen
    var xpLevel: Int = 3 // TODO Wert von DB holen
    var hasTitle: Boolean = true // TODO Wert von DB holen

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    SaftyAppLogo(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(start = 27.dp)
                    )

                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Open Drawer"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(85.dp),
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("XP Level $xpLevel", style = MaterialTheme.typography.titleMedium)

                        if(hasTitle) {
                            Text("Advanced Juicy Maker", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { (xpCurrent.toFloat() / xpTotal) },
                            modifier = Modifier
                                .width(300.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                        )

                        Text(
                            "$xpCurrent / $xpTotal",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .width(60.dp),
                            style = MaterialTheme.typography.bodySmall,
                            )
                    }

                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(R.drawable.safty_icon),
                    contentDescription = "Safty",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { Log.i("Button", "Mix is clicked") } // TODO
            ) {
                Text("Mix it!")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // TODO
                items(listOf("Salt", "Pepper", "Oil")) { ingredient ->
                    Text("- $ingredient")
                }
            }

            //Spacer(modifier = Modifier.height(16.dp))


        }
    }
}

@Composable
private fun SaftyAppLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painterResource(R.drawable.saftyapp_logo2_free),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.width(5.dp))
        Image(
            painter = painterResource(R.drawable.saftyapp_logo_text),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}