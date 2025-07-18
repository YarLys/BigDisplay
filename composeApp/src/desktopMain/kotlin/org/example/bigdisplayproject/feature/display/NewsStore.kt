package org.example.bigdisplayproject.feature.display

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.util.onError
import org.example.bigdisplayproject.feature.display.util.onSuccess

internal class NewsStoreFactory(
    private val storeFactory: StoreFactory,
    private val newsClient: NewsClient
) {   // когда-нибудь потом вынести storeFactory в DI

    fun create(): NewsStore =
        object : NewsStore, Store<NewsStore.Intent, NewsStore.State, Nothing> by storeFactory.create(
            name = "CounterStore",
            initialState = NewsStore.State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(newsClient) },
            reducer = ReducerImpl
        ) {}

    private class BootstrapperImpl : CoroutineBootstrapper<NewsStore.Action>() {
        override fun invoke() {
            dispatch(NewsStore.Action.LoadNews())
        }
    }

    private class ExecutorImpl(
        private val newsClient: NewsClient
    ) : CoroutineExecutor<NewsStore.Intent, NewsStore.Action, NewsStore.State, NewsStore.Message, Nothing>() {
        override fun executeAction(action: NewsStore.Action) {    // process actions from bootstrapper
            when (action) {
                is NewsStore.Action.LoadNews -> getNews()
            }
        }

        override fun executeIntent(intent: NewsStore.Intent) {
            when (intent) {
                is NewsStore.Intent.GetNews -> getNews()
                is NewsStore.Intent.GetNewsById -> getNewsById(intent.id)
                is NewsStore.Intent.Refresh -> refresh()
                is NewsStore.Intent.UpdateScrollPosition -> dispatch(NewsStore.Message.ScrollPositionUpdated(intent.position))
            }
        }

        private fun getNews() = scope.launch {
            dispatch(NewsStore.Message.Loading)
            val news = newsClient.getNews()
                .onSuccess {
                    dispatch(NewsStore.Message.NewsLoaded(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(NewsStore.Message.Error(it.toString()))
                }
        }

        private fun getNewsById(id: Long) = scope.launch {
            val news = newsClient.getNewsById(id)
                .onSuccess {
                    dispatch(NewsStore.Message.NewsSelected(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(NewsStore.Message.Error(it.toString()))
                }
        }

        private fun refresh() {
            getNews()
        }

    }

    private object ReducerImpl : Reducer<NewsStore.State, NewsStore.Message> {
        override fun NewsStore.State.reduce(msg: NewsStore.Message): NewsStore.State =
            when (msg) {
                is NewsStore.Message.NewsLoaded -> copy(
                    isLoading = false,
                    news = msg.news,
                    error = null
                )
                is NewsStore.Message.NewsSelected -> {
                    if (msg.news.id != selectedNews?.id) {
                        copy(
                            isLoading = false,
                            selectedNews = msg.news
                        )
                    }
                    else copy(isLoading = false)
                }
                is NewsStore.Message.Error -> copy(isLoading = false, error = msg.message)
                is NewsStore.Message.Loading -> copy(isLoading = true)
                is NewsStore.Message.ScrollPositionUpdated -> copy(scrollPosition = msg.position)
            }
    }
}
