package com.example.phenakistiscope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.phenakistiscope.ui.theme.PhenakistiscopeTheme
import kotlinx.coroutines.flow.Flow

internal class PhenakistiscopeMainActivity : ComponentActivity() {

    private val viewModel: PhenakistiscopeMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhenakistiscopeTheme {
                val mainScreenState by viewModel.currentScreenState.toStateWhenStarted(
                    initialValue = MainScreenState()
                )

                MainScreenContent(
                    mainScreenState = mainScreenState,
                    onAddFrameClicked = { frame -> viewModel.onAddFrameClicked(frame) },
                    onPlayClicked = { viewModel.onPlayClicked() },
                    onPauseClicked = { viewModel.onPauseClicked() },
                    onRemoveFrameClicked = { viewModel.onRemoveFrameClicked() },
                    frameEdited = { viewModel.frameEdited() },
                    onInstrumentClicked = { instrument -> viewModel.onInstrumentClicked(instrument) }
                )
            }
        }
    }
}

@Composable
internal fun <T> Flow<T>.toStateWhenStarted(initialValue: T): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    return produceState(initialValue = initialValue, this, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect { value = it }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun GreetingPreview() {
    PhenakistiscopeTheme {
        MainScreenContent(MainScreenState())
    }
}
