package org.example.bigdisplayproject.ui.components.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class VideoPlayerState {
    private var mediaPlayer: MediaPlayer? = null
    private val defferredEffects = mutableListOf<(MediaPlayer) -> Unit>()

    fun isMediaPlayerReady(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    fun doWithMediaPlayer(block: (MediaPlayer) -> Unit) {
        mediaPlayer?.let {
            block(it)

        } ?: run {
            defferredEffects.add(block)
        }

    }

    internal fun onMediaPlayerReady(mediaPlayer: uk.co.caprica.vlcj.player.base.MediaPlayer) {
        val mp = MediaPlayer(mediaPlayer)
        this.mediaPlayer = mp

        defferredEffects.forEach { block ->
            block(mp)
        }
        defferredEffects.clear()
    }
}

@Composable
fun rememberVideoPlayerState() = remember { VideoPlayerState() }