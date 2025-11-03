package org.example.bigdisplayproject.data.remote.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.example.bigdisplayproject.data.remote.dto.cursor.CursorData
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class CursorUdpService {
    private var socket: DatagramSocket? = null
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getCursorData(): Result<CursorData> = withContext(Dispatchers.IO) {
        try {
            ensureSocketConnected()

            // Отправка запроса
            val requestData = "getCursorData".toByteArray()
            val requestPacket = DatagramPacket(
                requestData,
                requestData.size,
                InetAddress.getByName("localhost"),
                4200
            )
            socket?.send(requestPacket)

            // Получение ответа
            val buffer = ByteArray(1024)
            val responsePacket = DatagramPacket(buffer, buffer.size)
            socket?.soTimeout = 5000
            socket?.receive(responsePacket)

            val responseString = String(responsePacket.data, 0, responsePacket.length)
            println("CURSOR: Cursor data received successfully")
            //println("CURSOR_DATA: $responseString")
            val cursorData = json.decodeFromString<CursorData>(responseString)
            Result.success(cursorData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ensureSocketConnected() {
        if (socket == null || socket?.isClosed == true) {
            socket = DatagramSocket().apply {
                soTimeout = 5000
            }
        }
    }

    fun close() {
        socket?.close()
    }
}