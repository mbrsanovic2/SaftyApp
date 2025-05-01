package com.example.saftyapp.presentation.uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.saftyapp.model.viewmodels.XPViewModel
import kotlinx.coroutines.delay

@Composable
fun BottomBarXP(modifier: Modifier = Modifier, viewModel: XPViewModel = viewModel()) {
    val userState by viewModel.userState.collectAsState()
    val hasTitle: Boolean = userState.isJUICY
    var showJuicyDialog by remember { mutableStateOf(false) }

    val xpGainToAnimate by viewModel.xpGainToAnimate.collectAsState()
    val displayedXP by viewModel.displayedXP.collectAsState()
    val displayedXPTarget by viewModel.displayedXPTarget.collectAsState()
    val displayedLevel by viewModel.displayedLevel.collectAsState()

    LaunchedEffect(userState.currentXP) {
        delay(700L)
        while(xpGainToAnimate > 0){
            viewModel.reduceGainAnim(1)
            viewModel.incrementXP()
            delay(200L)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.juicyUnlocked.collect {
            showJuicyDialog = true
        }
    }
    AdvancedJuicyMessage(
        showJuicyDialog,
        "Congrats you are now an advanced Juicy Maker, you have unlocked all ingredients and are now able to create your own recipes"
    ) { showJuicyDialog = false }

    BottomAppBar(
        modifier = modifier.height(IntrinsicSize.Min),
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
                Text("XP Level $displayedLevel", style = MaterialTheme.typography.titleMedium)

                if (hasTitle) {
                    Text(
                        "Advanced Juicy Maker",
                        style = MaterialTheme.typography.titleMedium
                    )
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
                    progress = { displayedXP.toFloat() / displayedXPTarget },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "$displayedXP / $displayedXPTarget",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall,
                )
                if (xpGainToAnimate > 0) {
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "+$xpGainToAnimate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun AdvancedJuicyMessage(
    showDialog: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 Level Up!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Nice!")
                    }
                }
            }
        }
    }
}