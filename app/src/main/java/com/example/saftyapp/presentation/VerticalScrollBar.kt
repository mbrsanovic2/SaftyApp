package com.example.saftyapp.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.drawVerticalScrollbar(
    state: LazyGridState,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
    width: Dp = 8.dp,
    cornerRadius: Dp = 3.dp
): Modifier = this.then(
    Modifier.drawBehind {
        val totalItems = state.layoutInfo.totalItemsCount
        val visibleItems = state.layoutInfo.visibleItemsInfo

        if (totalItems == 0 || visibleItems.isEmpty()) return@drawBehind

        val firstVisibleIndex = visibleItems.first().index
        val totalHeight = size.height

        val scrollbarHeight = totalHeight * (visibleItems.size.toFloat() / totalItems)
        val scrollbarOffset = (totalHeight - scrollbarHeight) * (firstVisibleIndex.toFloat() / (totalItems - visibleItems.size))

        drawRoundRect(
            color = color,
            topLeft = Offset(x = size.width - width.toPx(), y = scrollbarOffset),
            size = androidx.compose.ui.geometry.Size(width.toPx(), scrollbarHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx())
        )
    }
)

