package com.example.phenakistiscope

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GeneratePanel(
    modifier: Modifier = Modifier,
    mainScreenState: MainScreenState,
    onGenerateSelected: (Int) -> Unit,
    dismissClicked: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier
            .background(
                color = colorResource(id = R.color.panels_background_color),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_dimen))
            )
            .padding(all = dimensionResource(id = R.dimen.main_dimen)),
        onDismissRequest = dismissClicked
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var currentValue by remember { mutableStateOf("") }

            Text(modifier = Modifier, text = " It may take a long time")
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.main_dimen)))
            TextField(
                value = currentValue, onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull() ?: return@TextField
                    if (intValue <= Int.MAX_VALUE - mainScreenState.currentIndex && intValue > 0) {
                        currentValue = newValue
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.main_dimen)))

            if (mainScreenState.isGenerating) {
                CircularProgressIndicator()
            } else {
                Text(
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.main_color),
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_dimen))
                        )
                        .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_dimen)))
                        .clickable {
                            if (currentValue.toIntOrNull() != null) {
                                onGenerateSelected(currentValue.toInt())
                            }
                        }
                        .padding(horizontal = dimensionResource(id = R.dimen.main_dimen)),
                    text = "Generate"
                )
            }
        }
    }
}
