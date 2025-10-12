package org.example.bigdisplayproject.ui.slider

import VideoPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.ui.components.BottomPanel
import org.example.bigdisplayproject.ui.slider.store.SliderStore
import org.example.bigdisplayproject.ui.theme.DarkGray
import org.example.bigdisplayproject.ui.theme.GradientColor1
import org.example.bigdisplayproject.ui.theme.GradientColor10
import org.example.bigdisplayproject.ui.theme.GradientColor11
import org.example.bigdisplayproject.ui.theme.GradientColor12
import org.example.bigdisplayproject.ui.theme.GradientColor13
import org.example.bigdisplayproject.ui.theme.GradientColor14
import org.example.bigdisplayproject.ui.theme.GradientColor15
import org.example.bigdisplayproject.ui.theme.GradientColor2
import org.example.bigdisplayproject.ui.theme.GradientColor3
import org.example.bigdisplayproject.ui.theme.GradientColor4
import org.example.bigdisplayproject.ui.theme.GradientColor5
import org.example.bigdisplayproject.ui.theme.GradientColor6
import org.example.bigdisplayproject.ui.theme.GradientColor7
import org.example.bigdisplayproject.ui.theme.GradientColor8
import org.example.bigdisplayproject.ui.theme.GradientColor9
import org.example.bigdisplayproject.ui.theme.LightWhite
import rememberVideoPlayerState
import javax.swing.JButton
import javax.swing.JPanel

@Composable
fun Slider(
    onMenuButtonClick: () -> Unit,
    onNewsLinkClick: (Long) -> Unit,
    onScheduleLinkClick: () -> Unit,
    state: SliderStore.State
) {
    Scaffold(
        bottomBar = {
            BottomPanel(
                onButtonClick = onMenuButtonClick,
                text = "Меню",
                icon = Icons.Filled.Menu
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val gradient = Brush.radialGradient(
                        colors = listOf(
                            DarkGray,
                            GradientColor1,
                            GradientColor2,
                            GradientColor3,
                            GradientColor4,
                            GradientColor5,
                            GradientColor6,
                            GradientColor7,
                            GradientColor8,
                            GradientColor9,
                            GradientColor10,
                            GradientColor11,
                            GradientColor12,
                            GradientColor13,
                            GradientColor14,
                            GradientColor15
                        ),
                        radius = 1500f,
                        center = Offset(size.width / 2f - 150, size.height / 2f)
                    )
                    onDrawBehind {
                        rotate(50f, pivot = center) {
                            scale(1f, 2f, pivot = center) {
                                drawRect(
                                    brush = gradient,
                                    size = Size(size.width * 2, size.height * 2),
                                    topLeft = Offset(-size.width / 2, -size.height / 2)
                                )
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(
                    "Ошибка: ${state.error}",
                    color = LightWhite,
                    fontSize = 40.sp
                )

                state.slidesData != null -> {
                    val slides = state.slidesData.importantSlide + state.slidesData.defaultSlide

                    SlidesScreen(
                        slides = slides,
                        onNewsLinkClick = onNewsLinkClick,
                        onScheduleLinkClick = onScheduleLinkClick
                    )
                    //TestSwing()
                }

                else -> {
                    Text(
                        text = "Новости не найдены",
                        fontSize = 40.sp,
                        color = LightWhite
                    )
                }
            }
        }
    }
}

@Composable
fun TestSwing() {
    val state = rememberVideoPlayerState()

    Box(modifier = Modifier.fillMaxSize()) {
        SwingPanel(factory = {
            JPanel().also { panel ->
                panel.background = java.awt.Color.red
                panel.add(JButton().also { button ->
                    button.text = "JButton"
                })
            }
        }, modifier = Modifier.fillMaxSize())

        /*VideoPlayer(
            url = "https://storage.yandexcloud.net/media-screen/560255de7a2498a4ca653a13eb776f18e2ea53d3bffd83fc2bd8ce3310d9bc79.mp4",
            state = state,
            onFinish = state::stopPlayback,
            modifier = Modifier
                .fillMaxSize()
            //.height((950).pxToDp())
        )*/

        /*Snackbar(
            action = { Button(onClick = {}) { Text("OK") } },
            modifier = Modifier.padding(8.dp).align(Alignment.BottomCenter),
        ) {
            Text("Snackbar")
        }
        Popup(alignment = Alignment.Center) {
            Box(
                modifier = Modifier.size(200.dp, 100.dp).background(Color.Gray),
                contentAlignment = Alignment.Center,
            ) {

                Text("Popup")
            }

        }*/
    }
}