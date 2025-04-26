package com.example.saftyapp.presentation.uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.saftyapp.model.database.Repository
import com.example.saftyapp.model.viewmodels.XPViewModel
import kotlinx.coroutines.delay

@Composable
fun BottomBarXP(modifier: Modifier = Modifier, viewModel: XPViewModel = viewModel()) {
    val userState by viewModel.userState.collectAsState()
    val xpTotal: Int = userState.targetXP
    val xpCurrent: Int = userState.currentXP
    val xpLevel: Int = userState.level
    val hasTitle: Boolean = userState.isJUICY


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
                Text("XP Level $xpLevel", style = MaterialTheme.typography.titleMedium)

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
                    progress = { xpCurrent.toFloat() / xpTotal },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "$xpCurrent / $xpTotal",
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}