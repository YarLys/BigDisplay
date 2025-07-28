package org.example.bigdisplayproject.ui.slider.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData


interface SliderStore : Store<SliderStore.Intent, SliderStore.State, Nothing> {

    sealed interface Action {
        class LoadSlides(): Action
    }

    // Интенты
    sealed interface Intent : JvmSerializable {
        object GetSlides: Intent
    }

    // Сообщения от executor к reducer
    sealed interface Message {
        data class Error(val message: String): Message
        object Loading: Message
        data class SlidesLoaded(val slidesData: GroupSlidesData): Message
    }

    // Состояние
    data class State(
        val isLoading: Boolean = false,
        val slidesData: GroupSlidesData? = null,
        val error: String? = null,
    ) : JvmSerializable

}