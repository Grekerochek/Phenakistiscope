package com.example.phenakistiscope

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.phenakistiscope.ui.theme.PhenakistiscopeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // window.setStatusBarDarkIcons(false)
        setContent {
            PhenakistiscopeTheme {
                DrawScreen()
            }
        }
    }
}

/*
fun Window.setStatusBarDarkIcons(dark: Boolean) {
    when {
        Build.VERSION_CODES.R <= Build.VERSION.SDK_INT -> insetsController?.setSystemBarsAppearance(
            if (dark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        else -> decorView.systemUiVisibility = if (dark) {
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}
*/
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PhenakistiscopeTheme {
        DrawScreen()
    }
}
