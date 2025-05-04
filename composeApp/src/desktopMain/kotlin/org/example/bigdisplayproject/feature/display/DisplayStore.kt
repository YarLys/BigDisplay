package org.example.bigdisplayproject.feature.display

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.util.onError
import org.example.bigdisplayproject.feature.display.util.onSuccess

internal class DisplayStoreFactory(
    private val storeFactory: StoreFactory,
    private val newsClient: NewsClient
) {   // когда-нибудь потом вынести storeFactory в DI

    fun create(): DisplayStore =
        object : DisplayStore, Store<DisplayStore.Intent, DisplayStore.State, Nothing> by storeFactory.create(
            name = "CounterStore",
            initialState = DisplayStore.State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(newsClient) },
            reducer = ReducerImpl
        ) {}

    private class BootstrapperImpl : CoroutineBootstrapper<DisplayStore.Action>() {
        override fun invoke() {
            dispatch(DisplayStore.Action.LoadNews())
        }
    }

    private class ExecutorImpl(
        private val newsClient: NewsClient
    ) : CoroutineExecutor<DisplayStore.Intent, DisplayStore.Action, DisplayStore.State, DisplayStore.Message, Nothing>() {
        override fun executeAction(action: DisplayStore.Action) {    // process actions from bootstrapper
            when (action) {
                is DisplayStore.Action.LoadNews -> getNews()
            }
        }

        override fun executeIntent(intent: DisplayStore.Intent) {
            when (intent) {
                is DisplayStore.Intent.GetNews -> getNews()
                is DisplayStore.Intent.GetNewsById -> getNewsById(intent.id)
                is DisplayStore.Intent.Refresh -> refresh()
                is DisplayStore.Intent.UpdateScrollPosition -> dispatch(DisplayStore.Message.ScrollPositionUpdated(intent.position))
            }
        }

        private fun getNews() = scope.launch {
            dispatch(DisplayStore.Message.Loading)
            val news = newsClient.getNews()
                .onSuccess {
                    dispatch(DisplayStore.Message.NewsLoaded(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(DisplayStore.Message.Error(it.toString()))
                }
        }

        private fun getNewsById(id: Long) = scope.launch {
            /*val selected = state().news.find { it.id == id }
            selected?.let { dispatch(DisplayStore.Message.NewsSelected(it)) }*/
            dispatch(DisplayStore.Message.Loading)
            val news = newsClient.getNewsById(id)
                .onSuccess {
                    dispatch(DisplayStore.Message.NewsSelected(it))
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(DisplayStore.Message.Error(it.toString()))
                }
        }

        private fun refresh() {
            getNews()
        }

    }

    private object ReducerImpl : Reducer<DisplayStore.State, DisplayStore.Message> {
        override fun DisplayStore.State.reduce(msg: DisplayStore.Message): DisplayStore.State =
            when (msg) {
                is DisplayStore.Message.NewsLoaded -> copy(
                    isLoading = false,
                    news = msg.news,
                    error = null
                )
                is DisplayStore.Message.NewsSelected -> {
                    if (msg.news.id != selectedNews?.id) {
                        copy(
                            isLoading = false,
                            selectedNews = msg.news
                        )
                    }
                    else copy(isLoading = false)
                }
                is DisplayStore.Message.Error -> copy(isLoading = false, error = msg.message)
                is DisplayStore.Message.Loading -> copy(isLoading = true)
                is DisplayStore.Message.ScrollPositionUpdated -> copy(scrollPosition = msg.position)
            }
    }
}
