package org.example.bigdisplayproject

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.bigdisplayproject.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Fullscreen
        },
        undecorated = true
    ) {
        App()
    }
}