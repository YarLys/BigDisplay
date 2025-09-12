package org.example.bigdisplayproject.data.remote.dto.cursor

import kotlinx.serialization.Serializable

@Serializable
data class CursorData(
    val worldPosition: WorldPosition,
    val screenPosition: ScreenPosition,
    val currentGesture: Int
)

@Serializable
data class WorldPosition(
    val x: Double,
    val y: Double,
    val z: Double
)

@Serializable
data class ScreenPosition(
    val x: Double,
    val y: Double
)
