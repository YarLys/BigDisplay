package org.example.bigdisplayproject.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.http.isSuccess
import io.ktor.util.cio.use
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.copyTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

class FileDownloaderRepository {

    private val client = HttpClient(CIO)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeDownloads = mutableMapOf<String, Job>()

    suspend fun downloadFile(
        url: String,
        outputPath: String,
        onCompletion: (Result<Long>) -> Unit
    ) {
        // Проверяем, не скачан ли уже файл
        val file = File(outputPath)
        if (file.exists() && file.length() > 0) {
            onCompletion(Result.success(file.length()))
            return
        }

        // Проверяем, не идет ли уже загрузка этого файла
        if (activeDownloads.containsKey(outputPath)) {
            onCompletion(Result.failure(Exception("Download is already in progress")))
            return
        }

        val job = scope.launch {
            val result = kotlin.runCatching {
                val response = client.get(url)

                if (response.status.isSuccess()) {
                    val file = File(outputPath)
                    file.parentFile?.mkdirs()
                    response.bodyAsChannel().copyTo(file.writeChannel())

                    file.length()
                } else {
                    throw Exception("Error in downloading video file: ${response.status}")
                }
            }

            activeDownloads.remove(outputPath)
            onCompletion(result)
        }

        activeDownloads[outputPath] = job
    }

    fun close() {
        //scope.cancel()
        activeDownloads.values.forEach { it.cancel() }
        activeDownloads.clear()
        client.close()
    }

}