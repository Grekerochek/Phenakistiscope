package com.example.phenakistiscope.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.phenakistiscope.R

@Composable
internal fun SpeedPanel(
    modifier: Modifier = Modifier,
    onSpeedSelected: (Int) -> Unit,
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
        CurrentSpeed(speed = 1, onSpeedSelected = onSpeedSelected)
        CurrentSpeed(speed = 2, onSpeedSelected = onSpeedSelected)
        CurrentSpeed(speed = 3, onSpeedSelected = onSpeedSelected)
    }
}

@Composable
private fun CurrentSpeed(speed: Int, onSpeedSelected: (Int) -> Unit) {
    Text(
        text = speed.toString(),
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onSpeedSelected(speed)
            }
    )
}
