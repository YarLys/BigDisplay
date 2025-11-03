package org.example.bigdisplayproject.ui.slider.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.domain.usecases.common.DownloadFileUseCase
import org.example.bigdisplayproject.domain.usecases.slider.GetSlidesUseCase
import org.example.bigdisplayproject.domain.util.onError
import org.example.bigdisplayproject.domain.util.onSuccess
import org.example.bigdisplayproject.ui.news.store.NewsStore

internal class SliderStoreFactory(
    private val storeFactory: StoreFactory,
    private val getSlidesUseCase: GetSlidesUseCase,
    private val downloadFileUseCase: DownloadFileUseCase
) {

    fun create(): SliderStore =
        object : SliderStore,
            Store<SliderStore.Intent, SliderStore.State, Nothing> by storeFactory.create(
                name = "SliderStore",
                initialState = SliderStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = { ExecutorImpl(getSlidesUseCase, downloadFileUseCase) },
                reducer = ReducerImpl
            ) {}

    private class BootstrapperImpl : CoroutineBootstrapper<SliderStore.Action>() {
        override fun invoke() {
            dispatch(SliderStore.Action.LoadSlides)
        }
    }

    private class ExecutorImpl(
        private val getSlidesUseCase: GetSlidesUseCase,
        private val downloadFileUseCase: DownloadFileUseCase
    ) : CoroutineExecutor<SliderStore.Intent, SliderStore.Action, SliderStore.State, SliderStore.Message, Nothing>() {

        private var slidesRefreshJob: Job? = null

        override fun executeAction(action: SliderStore.Action) {    // process actions from bootstrapper
            when (action) {
                is SliderStore.Action.LoadSlides -> getSlides()
            }
        }

        override fun executeIntent(intent: SliderStore.Intent) {
            when (intent) {
                is SliderStore.Intent.GetSlides -> getSlides()
                is SliderStore.Intent.DownloadFile -> downloadFile(intent.url, intent.outputPath)
            }
        }

        private fun getSlides() = scope.launch {
            dispatch(SliderStore.Message.Loading)
            startPeriodicSlidesRefresh()
        }

        fun startPeriodicSlidesRefresh(intervalMillis: Long = 60 * 50 * 1000) {  // обновление новостей раз в 50 мин
            stopPeriodicSlidesRefresh() // Останавливаем предыдущую задачу

            slidesRefreshJob = CoroutineScope(Dispatchers.IO).launch {
                while (isActive) {
                    refreshSlides()
                    delay(intervalMillis)
                }
            }
        }

        fun stopPeriodicSlidesRefresh() {
            slidesRefreshJob?.cancel()
            slidesRefreshJob = null
        }

        private suspend fun refreshSlides() {
            try {
                getSlidesUseCase()
                    .onSuccess { slides ->
                        dispatch(SliderStore.Message.SlidesLoaded(slides))
                        downloadVideos(slides)
                        println("Slides refreshed at ${System.currentTimeMillis()}")
                    }
                    .onError { error ->
                        dispatch(SliderStore.Message.Error(error.toString()))
                        println("Error refreshing slides: $error")
                    }
            } catch (e: Exception) {
                println("Exception in slides refresh: ${e.message}")
            }
        }

        private fun downloadVideos(slidesData: GroupSlidesData) = scope.launch {
            var fileNum = 0
            val slides = slidesData.importantSlide + slidesData.defaultSlide
            for (slide in slides) {
                if (slide.mediaContent is SlideVideo) {
                    val url = slide.mediaContent.videoContent.src
                    fileNum++   // TODO нормальную нумерацию видео в слайдере. Но пока что там одно видео, к тому же оно отображается по ссылке
                    val outputPath = "D:\\Projects\\Kotlin\\Android\\KMP\\BigDisplayProject\\sliderVideo\\video${fileNum}.mp4"

                    downloadFile(url, outputPath)
                }
            }
        }

        private fun downloadFile(url: String, outputPath: String) = scope.launch {
            dispatch(SliderStore.Message.Loading)
            try {
                downloadFileUseCase(url, outputPath)
                dispatch(SliderStore.Message.FileDownloaded)
            } catch (e: Exception) {
                dispatch(SliderStore.Message.Error(e.message ?: "Unknown error"))
            }

        }
    }

    private object ReducerImpl : Reducer<SliderStore.State, SliderStore.Message> {
        override fun SliderStore.State.reduce(msg: SliderStore.Message): SliderStore.State =
            when (msg) {
                is SliderStore.Message.SlidesLoaded -> copy(
                    isLoading = false,
                    slidesData = msg.slidesData,
                    error = null
                )

                is SliderStore.Message.FileDownloaded -> copy(
                    isLoading = false,
                    error = null,
                    fileNum = fileNum + 1
                )

                is SliderStore.Message.Error -> copy(isLoading = false, error = msg.message)
                is SliderStore.Message.Loading -> copy(isLoading = true)
            }
    }
}
