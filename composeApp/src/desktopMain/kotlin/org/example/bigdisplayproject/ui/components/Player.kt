package org.example.bigdisplayproject.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.Locale
import javax.swing.BoxLayout
import javax.swing.JPanel

fun Player(): JPanel {
    val mediaPlayerComponent = initializeMediaPlayerComponent()
    val mediaPlayer = mediaPlayerComponent.mediaPlayer()

    return JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(mediaPlayerComponent)
    }
}

private fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()
    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

private fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}