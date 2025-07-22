package org.example.bigdisplayproject.ui.slider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.bigdisplayproject.ui.components.BottomPanel

@Composable
fun Slider(
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomPanel(onButtonClick = onMenuButtonClick, text = "Меню") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

        }
    }
}