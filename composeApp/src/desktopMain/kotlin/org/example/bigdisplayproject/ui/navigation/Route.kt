package org.example.bigdisplayproject.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object NewsGraph: Route

    @Serializable
    data object NewsList: Route

    @Serializable
    data class NewsDetail(val id: Long): Route

    @Serializable
    data object Slider: Route

    @Serializable
    data object Menu: Route

    @Serializable
    data object Schedule: Route
}