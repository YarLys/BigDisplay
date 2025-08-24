package org.example.bigdisplayproject.ui.slider.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import org.example.bigdisplayproject.data.remote.dto.slider.GroupSlidesData
import org.example.bigdisplayproject.data.remote.dto.slider.SlideVideo
import org.example.bigdisplayproject.domain.usecases.common.DownloadFileUseCase
import org.example.bigdisplayproject.domain.usecases.slider.GetSlidesUseCase
import org.example.bigdisplayproject.domain.util.onError
import org.example.bigdisplayproject.domain.util.onSuccess

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
            val slides = getSlidesUseCase()
                .onSuccess {
                    dispatch(SliderStore.Message.SlidesLoaded(it))
                    downloadVideos(it)
                }
                .onError {
                    println("ERROR: ${it.toString()}")
                    dispatch(SliderStore.Message.Error(it.toString()))
                }
        }

        private fun downloadVideos(slidesData: GroupSlidesData) = scope.launch {
            var fileNum = 0
            val slides = slidesData.importantSlide + slidesData.defaultSlide
            for (slide in slides) {
                if (slide.mediaContent is SlideVideo) {
                    val url = slide.mediaContent.videoContent.src
                    fileNum++   // TODO нормальную нумерацию видео
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
