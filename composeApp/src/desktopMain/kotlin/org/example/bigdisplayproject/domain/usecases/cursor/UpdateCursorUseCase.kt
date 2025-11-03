package org.example.bigdisplayproject.domain.usecases.cursor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.api.CursorUdpService
import org.example.bigdisplayproject.ui.cursor.CursorManager

class UpdateCursorUseCase(
    private val udpService: CursorUdpService,
    private val cursorManager: CursorManager
) {
    private var isActive = false
    private var job: Job? = null

    fun startCursorUpdates(scope: CoroutineScope) {
        if (isActive) return

        isActive = true
        job = scope.launch {
            while (isActive) {
                val result = udpService.getCursorData()
                    .onSuccess {
                        val cursorData = it
                        cursorManager.updateCursor(
                            cursorData.screenPosition,
                            cursorData.currentGesture
                        )
                    }
                    .onFailure {
                        println("CURSOR: Error in receiving data: ${it.message}")
                        // Можно добавить логику повторных попыток
                    }

                delay(16) // для плавности
            }
        }
    }

    fun stopCursorUpdates() {
        isActive = false
        job?.cancel()
        cursorManager.dispose()
    }
}