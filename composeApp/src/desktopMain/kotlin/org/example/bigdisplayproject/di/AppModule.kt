package org.example.bigdisplayproject.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.example.bigdisplayproject.data.remote.NewsClient
import org.example.bigdisplayproject.data.remote.ScheduleClient
import org.example.bigdisplayproject.data.remote.api.NewsApi
import org.example.bigdisplayproject.data.remote.api.ScheduleApi
import org.example.bigdisplayproject.data.repository.NewsRepository
import org.example.bigdisplayproject.data.repository.ScheduleRepository
import org.example.bigdisplayproject.domain.usecases.news.GetNewsByIdUseCase
import org.example.bigdisplayproject.domain.usecases.news.GetNewsUseCase
import org.example.bigdisplayproject.domain.usecases.news.NewsUseCases
import org.example.bigdisplayproject.domain.usecases.schedule.DownloadCalendarUseCase
import org.example.bigdisplayproject.domain.usecases.schedule.GetScheduleUseCase
import org.example.bigdisplayproject.domain.usecases.schedule.ScheduleUseCases
import org.example.bigdisplayproject.ui.news.store.NewsStoreFactory
import org.example.bigdisplayproject.ui.schedule.store.ScheduleStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val koinModule = module {

    singleOf(::NewsClient)
    singleOf(::ScheduleClient)

    singleOf(::NewsApi)
    singleOf(::ScheduleApi)

    singleOf(::NewsRepository)
    singleOf(::ScheduleRepository)

    singleOf(::GetNewsUseCase)
    singleOf(::GetNewsByIdUseCase)
    singleOf(::NewsUseCases)

    singleOf(::GetScheduleUseCase)
    singleOf(::DownloadCalendarUseCase)
    singleOf(::ScheduleUseCases)

    single {
        NewsStoreFactory(
            storeFactory = DefaultStoreFactory(),
            newsUseCases = get()
        ).create()
    }

    single {
        ScheduleStoreFactory(
            storeFactory = DefaultStoreFactory(),
            scheduleUseCases = get()
        ).create()
    }

}