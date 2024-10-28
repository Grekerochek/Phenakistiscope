package com.example.phenakistiscope.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.R

@Composable
internal fun StylePanel(
    modifier: Modifier = Modifier,
    currentColor: Color,
    onStyleSelected: (Float) -> Unit,
) {
    Row(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.panels_background_color),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_dimen))
            )
            .padding(all = dimensionResource(id = R.dimen.main_dimen)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.main_dimen)),
    ) {
        BoxStyle(color = currentColor, stoke = 8f, onStyleSelected = onStyleSelected)
        BoxStyle(color = currentColor, stoke = 16f, onStyleSelected = onStyleSelected)
        BoxStyle(color = currentColor, stoke = 32f, onStyleSelected = onStyleSelected)
    }
}

@Composable
private fun BoxStyle(color: Color, stoke: Float, onStyleSelected: (Float) -> Unit) {
    Box(modifier = Modifier.size(20.dp).clickable { onStyleSelected(stoke) }) {
        Box(
            modifier = Modifier.align(Alignment.Center)
                .size(stoke.dp)
                .clip(CircleShape)
                .background(color = color)
        )
    }
}
