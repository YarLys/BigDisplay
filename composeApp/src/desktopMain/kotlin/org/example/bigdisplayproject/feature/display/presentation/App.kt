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
import org.example.bigdisplayproject.feature.display.NewsStore
import org.example.bigdisplayproject.feature.display.NewsStoreFactory
import org.example.bigdisplayproject.feature.display.ScheduleStore
import org.example.bigdisplayproject.feature.display.ScheduleStoreFactory
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.network.ScheduleClient
import org.example.bigdisplayproject.feature.display.network.dto.schedule.Classroom
import org.example.bigdisplayproject.feature.display.network.dto.schedule.ScheduleData
import org.example.bigdisplayproject.feature.display.presentation.menu.Menu
import org.example.bigdisplayproject.feature.display.presentation.newsdetails.NewsDetails
import org.example.bigdisplayproject.feature.display.presentation.newslist.NewsList
import org.example.bigdisplayproject.feature.display.presentation.navigation.Route
import org.example.bigdisplayproject.feature.display.presentation.schedule.Schedule
import org.example.bigdisplayproject.feature.display.presentation.slider.Slider


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App(
    newsClient: NewsClient,
    scheduleClient: ScheduleClient
) {
    MaterialTheme {
        val newsStore = remember {
            NewsStoreFactory(
                storeFactory = DefaultStoreFactory(),
                newsClient
            ).create()
        }
        val newsState by newsStore.stateFlow.collectAsState()

        val scheduleStore = remember {
            ScheduleStoreFactory(
                storeFactory = DefaultStoreFactory(),
                scheduleClient
            ).create()
        }
        val scheduleState by scheduleStore.stateFlow.collectAsState()

        val navController = rememberNavController()

        val listState = rememberLazyStaggeredGridState(
            initialFirstVisibleItemIndex = newsState.scrollPosition,
            initialFirstVisibleItemScrollOffset = newsState.scrollPosition + 1
        )
        LaunchedEffect(listState.isScrollInProgress) {
            snapshotFlow {
                listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
            }.collect { position ->
                newsStore.accept(NewsStore.Intent.UpdateScrollPosition(position.first))
            }
        }

        NavHost(
            navController = navController,
            startDestination = Route.NewsGraph
        ) {
            navigation<Route.NewsGraph>(
                startDestination = Route.Menu
            ) {
                composable<Route.NewsList> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            newsState.isLoading -> CircularProgressIndicator()
                            newsState.error != null -> Text("Ошибка: ${newsState.error}")
                            else -> {
                                NewsList(
                                    newsList = newsState.news,
                                    listState = listState,
                                    onItemClick = { id ->
                                        newsStore.accept(NewsStore.Intent.GetNewsById(id))  // предзагружаем данные
                                        navController.navigate(Route.NewsDetail(id)) {
                                            popUpTo(Route.NewsList) {
                                                saveState = true
                                            }
                                            restoreState = true
                                        }
                                    },
                                    onButtonClick = {
                                        navController.navigateUp()
                                    },
                                    /*isRefreshing = false,
                                    onRefresh = {
                                        store.accept(NewsStore.Intent.Refresh)
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
                                store.accept(NewsStore.Intent.GetNewsById(args.id))
                            }*/

                            if (newsState.error != null) {
                                Text("Ошибка: ${newsState.error}")
                            } else if (newsState.isLoading) {
                                CircularProgressIndicator()
                            } else newsState.selectedNews?.let { news ->
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
                composable<Route.Slider>(
                    // todo: animations
                ) {
                    Slider(
                        onMenuButtonClick = {
                            navController.navigate(Route.Menu)
                        }
                    )
                }
                composable<Route.Menu>(
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
                    Menu(
                        navController = navController
                    )
                }
                composable<Route.Schedule> {
                    Schedule(
                        onBackButtonClick = { navController.navigateUp() },
                        state = scheduleState,
                        getSchedule = { name ->
                            scheduleStore.accept(ScheduleStore.Intent.GetSchedule(name))
                        },
                        getCalendarData = { url ->
                            scheduleStore.accept(ScheduleStore.Intent.DownloadCalendar(url))
                        }
                    )
                }
            }
        }
    }
}