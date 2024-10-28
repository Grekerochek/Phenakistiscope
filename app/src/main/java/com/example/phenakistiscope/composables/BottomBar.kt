package com.example.phenakistiscope.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.phenakistiscope.CurrentScreen
import com.example.phenakistiscope.Instrument
import com.example.phenakistiscope.InstrumentState.DISABLED
import com.example.phenakistiscope.InstrumentState.ENABLED
import com.example.phenakistiscope.InstrumentState.SELECTED
import com.example.phenakistiscope.MainScreenState
import com.example.phenakistiscope.R

@Composable
internal fun BottomBar(
    mainScreenState: MainScreenState,
    onInstrumentClicked: (Instrument) -> Unit,
    onPalletOpened: () -> Unit,
    onStylePanelOpened: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = colorResource(id = R.color.main_color)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onInstrumentClicked(Instrument.Pencil) },
            imageVector = ImageVector.vectorResource(id = R.drawable.pencil),
            contentDescription = "pencil",
            tint = colorResource(
                id = when (mainScreenState.pencilState) {
                    SELECTED -> R.color.selected_icon_color
                    ENABLED -> R.color.enabled_icon_color
                    DISABLED -> R.color.disabled_icon_color
                }
            ),
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onInstrumentClicked(Instrument.Eraser) },
            imageVector = ImageVector.vectorResource(id = R.drawable.erase),
            contentDescription = "erase",
            tint = colorResource(
                id = when (mainScreenState.eraserState) {
                    SELECTED -> R.color.selected_icon_color
                    ENABLED -> R.color.enabled_icon_color
                    DISABLED -> R.color.disabled_icon_color
                }
            ),
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

        // open select color and style panels
        if (mainScreenState.currentScreen == CurrentScreen.Edit) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(2.dp, colorResource(id = R.color.selected_color), CircleShape)
                    .padding(1.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (mainScreenState.currentInstrument == Instrument.Eraser) {
                            mainScreenState.previousColor
                        } else {
                            mainScreenState.currentColor
                        }
                    )
                    .clickable { onPalletOpened() }
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.main_dimen)))

            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onStylePanelOpened()
                    }
                    .padding(all = 2.dp),
                text = "St",
                color = colorResource(id = R.color.enabled_icon_color)
            )
        }
    }
}
