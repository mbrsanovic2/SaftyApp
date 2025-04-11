package com.example.saftyapp.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    onOpenDrawer: () -> Unit
) {
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
        /*bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Button(
                    onClick = {
                        Log.i("Button", "Submit button clicked")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }*/
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome to Safty App",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // TODO Inhalt einfügen
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