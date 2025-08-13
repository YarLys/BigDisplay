package org.example.bigdisplayproject.ui

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.example.bigdisplayproject.ui.news.store.NewsStore
import org.example.bigdisplayproject.ui.schedule.store.ScheduleStore
import org.example.bigdisplayproject.di.koinModule
import org.example.bigdisplayproject.ui.menu.Menu
import org.example.bigdisplayproject.ui.news.newsdetails.NewsDetails
import org.example.bigdisplayproject.ui.news.newslist.NewsList
import org.example.bigdisplayproject.ui.navigation.Route
import org.example.bigdisplayproject.ui.schedule.Schedule
import org.example.bigdisplayproject.ui.slider.Slider
import org.example.bigdisplayproject.ui.slider.store.SliderStore
import org.koin.compose.getKoin
import org.koin.core.context.startKoin


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App() {
    startKoin {
        modules(koinModule)
    }

    MaterialTheme {
        val newsStore: NewsStore = getKoin().get()
        val newsState by newsStore.stateFlow.collectAsState()

        val scheduleStore: ScheduleStore = getKoin().get()
        val scheduleState by scheduleStore.stateFlow.collectAsState()

        val sliderStore: SliderStore = getKoin().get()
        val sliderState by sliderStore.stateFlow.collectAsState()

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
            // TODO: Переписать, чтобы было как в ветке Schedule
            navigation<Route.NewsGraph>(
                startDestination = Route.Slider
            ) {
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
                composable<Route.Slider> {
                    Slider(
                        state = sliderState,
                        onMenuButtonClick = {
                            navController.navigate(Route.Menu)
                        },
                        onNewsLinkClick = { id ->
                            newsStore.accept(NewsStore.Intent.GetNewsById(id))
                            navController.navigate(Route.NewsDetail(id))
                        },
                        onScheduleLinkClick = {
                            navController.navigate(Route.Schedule)
                        }
                    )
                }
                composable<Route.Schedule> {
                    Schedule(
                        state = scheduleState,
                        onBackButtonClick = {
                            navController.navigateUp()
                            scheduleStore.accept(ScheduleStore.Intent.ClearEvents)
                        },
                        getSchedule = { name ->
                            scheduleStore.accept(ScheduleStore.Intent.GetSchedule(name))
                        },
                        getCalendarData = { url ->
                            scheduleStore.accept(ScheduleStore.Intent.DownloadCalendar(url))
                        },
                        parseCalendar = { calendarData ->
                            scheduleStore.accept(ScheduleStore.Intent.ParseCalendar(calendarData))
                        },
                        getEvents = { events, date ->
                            scheduleStore.accept(ScheduleStore.Intent.GetEvents(events, date))
                        }
                    )
                }
                composable<Route.NewsList> {
                    NewsList(
                        state = newsState,
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
                            // TODO: Не понимаю, почему не работает. Нужно, чтобы при следующем заходе новости отображались сначала
                            newsStore.accept(NewsStore.Intent.UpdateScrollPosition(0))
                        },
                        /*isRefreshing = false,
                        onRefresh = {
                            store.accept(NewsStore.Intent.Refresh)
                        },*/
                    )
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
            }
        }
    }
}