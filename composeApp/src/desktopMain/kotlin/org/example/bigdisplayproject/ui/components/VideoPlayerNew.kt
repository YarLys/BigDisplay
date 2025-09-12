import androidx.compose.foundation.layout.size
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.bigdisplayproject.ui.components.VideoPlayerNew
import org.jetbrains.compose.resources.painterResource

data class Progress(
    val fraction: Float,
    // TODO: Use kotlin.time.Duration when Kotlin version is updated.
    //  See https://github.com/Kotlin/api-guidelines/issues/6
    val timeMillis: Long
)

@Composable
fun VideoPlayer(
    url: String,
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)? = null
) = VideoPlayerNew(
    url = url,
    isResumed = state.isResumed,
    volume = state.volume,
    speed = state.speed,
    seek = state.seek,
    isFullscreen = state.isFullscreen,
    progressState = state._progress,
    modifier = modifier,
    onFinish = onFinish
)

@Composable
fun rememberVideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false
): VideoPlayerState = rememberSaveable(saver = VideoPlayerState.Saver()) {
    VideoPlayerState(
        seek,
        speed,
        volume,
        isResumed,
        isFullscreen,
        Progress(0f, 0)
    )
}

class VideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false,
    progress: Progress
) {

    var seek by mutableStateOf(seek)
    var speed by mutableStateOf(speed)
    var volume by mutableStateOf(volume)
    var isResumed by mutableStateOf(isResumed)
    var isFullscreen by mutableStateOf(isFullscreen)
    internal val _progress = mutableStateOf(progress)
    val progress: State<Progress> = _progress

    fun toggleResume() {
        isResumed = !isResumed
    }

    fun toggleFullscreen() {
        isFullscreen = !isFullscreen
    }

    fun stopPlayback() {
        isResumed = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [VideoPlayerState].
         */
        fun Saver() = listSaver<VideoPlayerState, Any>(
            save = {
                listOf(
                    it.seek,
                    it.speed,
                    it.volume,
                    it.isResumed,
                    it.isFullscreen,
                    it.progress.value
                )
            },
            restore = {
                VideoPlayerState(
                    seek = it[0] as Float,
                    speed = it[1] as Float,
                    volume = it[2] as Float,
                    isResumed = it[3] as Boolean,
                    isFullscreen = it[3] as Boolean,
                    progress = it[4] as Progress,
                )
            }
        )
    }
}

@Composable
fun Speed(
    initialValue: Float,
    modifier: Modifier = Modifier,
    onChange: (Float?) -> Unit
) {
    var input by remember { mutableStateOf(initialValue.toString()) }
    OutlinedTextField(
        value = input,
        modifier = modifier,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Speed,
                contentDescription = "Speed",
                modifier = Modifier.size(28.dp)
            )
        },
        onValueChange = {
            input = if (it.isEmpty()) {
                it
            } else if (it.toFloatOrNull() == null) {
                input // Old value
            } else {
                it // New value
            }
            onChange(input.toFloatOrNull())
        }
    )
}