package org.example.bigdisplayproject.feature.display

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.presentation.components.NewsDetails
import org.example.bigdisplayproject.feature.display.presentation.components.NewsList
import org.example.bigdisplayproject.feature.display.presentation.navigation.Route

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App(client: NewsClient) {
    MaterialTheme {

        val store = remember {
            DisplayStoreFactory(storeFactory = DefaultStoreFactory(), client).create()
        }
        val state by store.stateFlow.collectAsState()

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.NewsGraph
        ) {
            navigation<Route.NewsGraph>(
                startDestination = Route.NewsList
            ) {
                composable<Route.NewsList> (
                    exitTransition = { slideOutHorizontally { it } },
                    popEnterTransition = { slideInHorizontally { it } }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        when {
                            state.isLoading -> CircularProgressIndicator()
                            state.error != null -> Text("Ошибка: ${state.error}")
                            else -> {
                                NewsList(
                                    news = state.news,
                                    onItemClick = { id ->
                                        navController.navigate(
                                            Route.NewsDetail(id)
                                        ) {
                                            popUpTo(Route.NewsList) {
                                                saveState = true // Сохраняем состояние списка
                                            }
                                            restoreState = true // Восстанавливаем при возврате
                                        }
                                    },
                                    isRefreshing = false,
                                    onRefresh = {
                                        store.accept(DisplayStore.Intent.Refresh)
                                    }
                                )
                            }
                        }
                    }
                }
                composable<Route.NewsDetail>(
                    enterTransition = { fadeIn() },
                    exitTransition = { fadeOut() }
                ) { entry ->
                    val args = entry.toRoute<Route.NewsDetail>()

                    LaunchedEffect(Unit) {
                        println("SELECTED NEWS_ID: ${args.id}")
                    }   // debug

                    LaunchedEffect(args.id) {
                        store.accept(DisplayStore.Intent.GetNewsById(args.id))
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        if (state.error != null) { Text("Ошибка: ${state.error}") }
                        else if (state.isLoading) { CircularProgressIndicator() }
                        else state.selectedNews?.let { news ->
                            NewsDetails(
                                news = news,
                                onBackButtonClick = {
                                    navController.navigateUp()
                                }
                            )
                        } ?: Text("Новость не найдена")
                    }
                }
            }
        }
    }
}