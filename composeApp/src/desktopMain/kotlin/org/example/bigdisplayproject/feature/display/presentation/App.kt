package org.example.bigdisplayproject.feature.display.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.example.bigdisplayproject.feature.display.DisplayStore
import org.example.bigdisplayproject.feature.display.DisplayStoreFactory
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

        val listState = rememberLazyStaggeredGridState(
            initialFirstVisibleItemIndex = state.scrollPosition,
            initialFirstVisibleItemScrollOffset = state.scrollPosition + 1
        )
        LaunchedEffect(listState.isScrollInProgress) {
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }.collect { position ->
                store.accept(DisplayStore.Intent.UpdateScrollPosition(position.first))
            }
        }

        NavHost(
            navController = navController,
            startDestination = Route.NewsGraph
        ) {
            navigation<Route.NewsGraph>(
                startDestination = Route.NewsList
            ) {
                composable<Route.NewsList> (
                    enterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    },
                    exitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    },
                    popEnterTransition = {
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    },
                    popExitTransition = {
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            state.isLoading -> CircularProgressIndicator()
                            state.error != null -> Text("Ошибка: ${state.error}")
                            else -> {
                                NewsList(
                                    newsList = state.news,
                                    listState = listState,
                                    onItemClick = { id ->
                                        store.accept(DisplayStore.Intent.GetNewsById(id))  // предзагружаем данные
                                        navController.navigate(Route.NewsDetail(id)) {
                                            popUpTo(Route.NewsList) {
                                                saveState = true
                                            }
                                            restoreState = true
                                        }
                                    },
                                    /*isRefreshing = false,
                                    onRefresh = {
                                        store.accept(DisplayStore.Intent.Refresh)
                                    },*/
                                )
                            }
                        }
                    }
                }
                dialog<Route.NewsDetail>(
                    dialogProperties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        dismissOnBackPress = true,
                        dismissOnClickOutside = true
                    )
                ) { entry ->
                    val args = entry.toRoute<Route.NewsDetail>()

                    var isDialogVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        isDialogVisible = true
                    }

                    DisposableEffect(Unit) {
                        onDispose { isDialogVisible = false }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(
                            visible = isDialogVisible,
                            enter = fadeIn(animationSpec = tween(700)) +
                                    scaleIn(initialScale = 0.9f, animationSpec = tween(500)),
                            exit = fadeOut(animationSpec = tween(700)) +
                                    scaleOut(targetScale = 0.9f, animationSpec = tween(500)),
                            modifier = Modifier.fillMaxSize()
                        ) {

                            LaunchedEffect(Unit) {
                                println("SELECTED NEWS_ID: ${args.id}")
                            }   // debug
                            /*LaunchedEffect(args.id) {
                                store.accept(DisplayStore.Intent.GetNewsById(args.id))
                            }*/

                            if (state.error != null) {
                                Text("Ошибка: ${state.error}")
                            } else if (state.isLoading) {
                                CircularProgressIndicator()
                            } else state.selectedNews?.let { news ->
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
}