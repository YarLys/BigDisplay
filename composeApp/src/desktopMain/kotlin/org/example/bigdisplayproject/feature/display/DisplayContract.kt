package org.example.bigdisplayproject.feature.display

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import org.example.bigdisplayproject.feature.display.network.dto.News

internal interface DisplayStore : Store<DisplayStore.Intent, DisplayStore.State, Nothing> {

    sealed interface Action {
        class LoadNews(): Action
    }

    // Интенты
    sealed interface Intent : JvmSerializable {
        object GetNews : Intent
        data class GetNewsById(val id: Long) : Intent
        object Refresh : Intent
    }

    // Сообщения от executor к reducer
    sealed interface Message {
        data class NewsLoaded(val news: List<News>) : Message
        data class NewsSelected(val news: News) : Message
        data class Error(val message: String) : Message
        object Loading : Message
    }

    // Состояние
    data class State(
        val isLoading: Boolean = false,
        val news: List<News> = emptyList(),
        val selectedNews: News? = null,
        val error: String? = null
    ) : JvmSerializable

}