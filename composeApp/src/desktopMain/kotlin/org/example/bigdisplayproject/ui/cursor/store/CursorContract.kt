package org.example.bigdisplayproject.ui.cursor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import org.example.bigdisplayproject.data.remote.dto.cursor.ScreenPosition
import org.example.bigdisplayproject.ui.cursor.CursorType


interface CursorStore : Store<CursorStore.Intent, CursorStore.State, Nothing> {

    sealed interface Action {
        object StartCursorUpdates: Action
    }

    sealed interface Intent : JvmSerializable {
        object StartCursorUpdates : Intent
        object StopCursorUpdates : Intent
    }

    sealed interface Message {
        object CursorUpdatesStarted: Message
        object CursorUpdatesStopped: Message
        data class Error(val message: String): Message
    }

    data class State(
        val isCursorActive: Boolean = false,
        val error: String? = null,
        val cursorType: CursorType = CursorType.DEFAULT,
        val cursorPosition: ScreenPosition = ScreenPosition(0.0, 0.0)
    ) : JvmSerializable

}