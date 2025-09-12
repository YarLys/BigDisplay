package org.example.bigdisplayproject.domain.usecases.cursor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.api.CursorUdpService
import org.example.bigdisplayproject.data.remote.dto.cursor.CursorData
import org.example.bigdisplayproject.ui.cursor.CursorManager
import java.io.File
import javax.imageio.ImageIO

class UpdateCursorUseCase(
    private val udpService: CursorUdpService,
    private val cursorManager: CursorManager
) {
    private var isActive = false
    private var job: Job? = null

    fun startCursorUpdates(scope: CoroutineScope) {
        if (isActive) return

        val cursorImage = ImageIO.read(File("D:\\Projects\\Kotlin\\Android\\KMP\\BigDisplayProject\\composeApp\\src\\commonMain\\composeResources\\drawable\\customCursorMove.png"))
        cursorManager.setCustomCursor(cursorImage)
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
    }
}