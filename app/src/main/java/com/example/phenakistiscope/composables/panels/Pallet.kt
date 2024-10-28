package com.example.phenakistiscope.composables.panels

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.R

@Composable
internal fun Pallet(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
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
        BoxColor(color = Color.Red, onColorSelected = onColorSelected)
        BoxColor(color = Color.Blue, onColorSelected = onColorSelected)
        BoxColor(color = Color.Green, onColorSelected = onColorSelected)
        BoxColor(color = Color.Black, onColorSelected = onColorSelected)
    }
}

@Composable
private fun BoxColor(color: Color, onColorSelected: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color = color)
            .clickable { onColorSelected(color) }
    )
}
