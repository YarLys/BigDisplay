package org.example.bigdisplayproject.domain.usecases.common

import org.example.bigdisplayproject.data.repository.FileDownloaderRepository

class DownloadFileUseCase(
    private val fileDownloaderRepository: FileDownloaderRepository
) {

    suspend operator fun invoke(url: String, outputPath: String) {
        try {
            fileDownloaderRepository.downloadFile(
                url,
                outputPath,
                onCompletion = { result ->
                    result.fold(
                        onSuccess = { println("Video file successfully downloaded: $outputPath") },
                        onFailure = { println("Video error: ${it.message}") }
                    )
                }
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }

}