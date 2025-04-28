package org.example.bigdisplayproject.feature.display.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object NewsGraph: Route

    @Serializable
    data object NewsList: Route

    @Serializable
    data class NewsDetail(val id: Long): Route
}