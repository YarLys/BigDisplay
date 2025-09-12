package org.example.bigdisplayproject.ui.cursor.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import org.example.bigdisplayproject.domain.usecases.cursor.UpdateCursorUseCase

internal class CursorStoreFactory(
    private val storeFactory: StoreFactory,
    private val updateCursorUseCase: UpdateCursorUseCase
) {

    fun create(): CursorStore =
        object : CursorStore,
            Store<CursorStore.Intent, CursorStore.State, Nothing> by storeFactory.create(
                name = "CursorStore",
                initialState = CursorStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl(updateCursorUseCase) },
                reducer = ReducerImpl
            ) {}

    private class BootstrapperImpl : CoroutineBootstrapper<CursorStore.Action>() {
        override fun invoke() {
            println("CURSOR_STORE: Bootstrapper")
            dispatch(CursorStore.Action.StartCursorUpdates)
        }
    }

    class ExecutorImpl(
        private val updateCursorUseCase: UpdateCursorUseCase
    ) : CoroutineExecutor<CursorStore.Intent, CursorStore.Action, CursorStore.State, CursorStore.Message, Nothing>() {

        override fun executeAction(action: CursorStore.Action) {
            when (action) {
                is CursorStore.Action.StartCursorUpdates -> startCursorUpdates()
            }
        }

        override fun executeIntent(intent: CursorStore.Intent) {
            when (intent) {
                is CursorStore.Intent.StartCursorUpdates -> startCursorUpdates()
                is CursorStore.Intent.StopCursorUpdates -> stopCursorUpdates()
            }
        }

        private fun startCursorUpdates() {
            println("CURSOR_STORE: Starting cursor updates")
            updateCursorUseCase.startCursorUpdates(scope)
            dispatch(CursorStore.Message.CursorUpdatesStarted)
        }

        private fun stopCursorUpdates() {
            updateCursorUseCase.stopCursorUpdates()
            dispatch(CursorStore.Message.CursorUpdatesStopped)
        }

    }

    private object ReducerImpl : Reducer<CursorStore.State, CursorStore.Message> {
        override fun CursorStore.State.reduce(msg: CursorStore.Message): CursorStore.State =
            when (msg) {
                is CursorStore.Message.CursorUpdatesStarted -> copy(isCursorActive = true)
                is CursorStore.Message.CursorUpdatesStopped -> copy(isCursorActive = false)
                is CursorStore.Message.Error -> copy(error = msg.message)
            }
    }
}
