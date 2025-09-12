package org.example.bigdisplayproject.ui.cursor.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable


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
        val error: String? = null
    ) : JvmSerializable

}