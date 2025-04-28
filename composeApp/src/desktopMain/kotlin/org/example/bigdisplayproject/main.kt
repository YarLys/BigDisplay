package org.example.bigdisplayproject

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.ktor.client.engine.cio.CIO
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.network.createHttpClient
import org.example.bigdisplayproject.feature.display.presentation.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
    ) {
        App(
            client = remember {
                NewsClient(createHttpClient(CIO))
            }
        )
    }
}