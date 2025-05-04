package org.example.bigdisplayproject.feature.display.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component

@Composable
fun VideoPlayer1(url: String) {
    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }

    val factory = remember { { mediaPlayerComponent } }
    LaunchedEffect(url) { mediaPlayer.media().play/*OR .start*/(url) }
    DisposableEffect(Unit) { onDispose(mediaPlayer::release) }
    SwingPanel(
        factory = factory,
        modifier = Modifier.fillMaxWidth(),
        update = {
        }
    )
}

private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}