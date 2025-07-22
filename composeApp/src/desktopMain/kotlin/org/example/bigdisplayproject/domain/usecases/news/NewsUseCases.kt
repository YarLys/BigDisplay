package org.example.bigdisplayproject.domain.usecases.news

data class NewsUseCases(
    val getNewsUseCase: GetNewsUseCase,
    val getNewsByIdUseCase: GetNewsByIdUseCase
)
