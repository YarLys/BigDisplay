package org.example.bigdisplayproject.feature.display.presentation.menu

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bigdisplayproject.composeapp.generated.resources.Res
import bigdisplayproject.composeapp.generated.resources.iit_logo_svg
import org.example.bigdisplayproject.feature.display.presentation.components.BottomPanel
import org.example.bigdisplayproject.feature.display.presentation.components.myShadow
import org.example.bigdisplayproject.feature.display.presentation.navigation.Route
import org.example.bigdisplayproject.feature.display.presentation.util.pxToDp
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
import org.jetbrains.compose.resources.painterResource

@Composable
fun Menu(
    navController: NavController
) {
    Scaffold(
        bottomBar = { BottomPanel(onButtonClick = { navController.navigateUp() }) }
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
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(top = (140).pxToDp(), start = (164).pxToDp(), end = (128).pxToDp(), bottom = (200).pxToDp()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((164).pxToDp())
            ) {
                Image(painterResource(Res.drawable.iit_logo_svg), null)
                Column(
                    verticalArrangement = Arrangement.spacedBy((20).pxToDp()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MenuCard(
                        text = "Новости",
                        onCardClick = { navController.navigate(Route.NewsList) },
                        modifier = Modifier.weight(1f)
                    )
                    MenuCard(
                        text = "Расписание",
                        onCardClick = { navController.navigate(Route.Schedule) },
                        modifier = Modifier.weight(1f)
                    )
                    MenuCard(
                        text = "Видео",
                        onCardClick = { /* todo: videos? */ },
                        modifier = Modifier.weight(1f)
                    )
                    MenuCard(
                        text = "Игры",
                        onCardClick = { /* todo: games */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuCard(
    text: String,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.4f else 1f,
        animationSpec = tween(300),
        label = "scaleAnimation"
    )

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .hoverable(interactionSource)
            .height(100.dp)
            .graphicsLayer {
                scaleY = scale
                transformOrigin = TransformOrigin(0.5f, 0.5f)
                shape = RoundedCornerShape(30.dp)
                clip = true
            }
            .shadow(
                elevation = if (isHovered) 30.dp else 8.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = if (isHovered) Color.White else Color.Black,
                ambientColor = if (isHovered) Color.White.copy(alpha = 0.7f) else Color.Black
            )
            .shadow(
                elevation = if (isHovered) 30.dp else 0.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = Color.White.copy(0.9f),
                ambientColor = Color.White.copy(alpha = 0.4f)
            )
            .shadow(
                elevation = if (isHovered) 30.dp else 0.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = Color.Cyan.copy(alpha = 0.9f),
                ambientColor = Color.Cyan.copy(alpha = 0.7f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onCardClick() },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightWhite,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleY = 1f / scale   // компенсируем масштабирование для текста
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = DarkGray,
                modifier = Modifier.wrapContentSize()
            )
        }
    }
}