package com.example.saftyapp.presentation.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.saftyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(showMenu: Boolean, onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            SaftyAppLogo(
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 27.dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                enabled = showMenu
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Open Drawer",
                    tint = if(showMenu) LocalContentColor.current else Color.Transparent
                )
            }
        }
    )
}

@Composable
private fun SaftyAppLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.saftyapp_logo2_free),
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