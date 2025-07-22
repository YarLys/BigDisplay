package org.example.bigdisplayproject.ui.news.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import org.example.bigdisplayproject.data.remote.dto.news.News

interface NewsStore : Store<NewsStore.Intent, NewsStore.State, Nothing> {

    sealed interface Action {
        class LoadNews(): Action
    }

    // Интенты
    sealed interface Intent : JvmSerializable {
        object GetNews : Intent
        data class GetNewsById(val id: Long) : Intent
        object Refresh : Intent
        data class UpdateScrollPosition(val position: Int) : Intent
    }

    // Сообщения от executor к reducer
    sealed interface Message {
        data class NewsLoaded(val news: List<News>) : Message
        data class NewsSelected(val news: News) : Message
        data class Error(val message: String) : Message
        object Loading : Message
        data class ScrollPositionUpdated(val position: Int) : Message
    }

    // Состояние
    data class State(
        val isLoading: Boolean = false,
        val news: List<News> = emptyList(),
        val selectedNews: News? = null,
        val error: String? = null,
        val scrollPosition: Int = 0
    ) : JvmSerializable

}