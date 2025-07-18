package org.example.bigdisplayproject

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.ktor.client.engine.cio.CIO
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.network.ScheduleClient
import org.example.bigdisplayproject.feature.display.network.createScheduleHttpClient
import org.example.bigdisplayproject.feature.display.network.createNewsHttpClient
import org.example.bigdisplayproject.feature.display.presentation.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BigDisplayProject",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Fullscreen
        },
        undecorated = true
    ) {
        App(
            newsClient = remember {
                NewsClient(createNewsHttpClient(CIO))
            },
            scheduleClient = remember {
                ScheduleClient(createScheduleHttpClient(CIO))
            }
        )
    }
}