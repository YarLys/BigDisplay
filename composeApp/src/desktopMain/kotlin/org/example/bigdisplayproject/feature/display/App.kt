package org.example.bigdisplayproject.feature.display.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.example.bigdisplayproject.feature.display.DisplayStore
import org.example.bigdisplayproject.feature.display.DisplayStoreFactory
import org.example.bigdisplayproject.feature.display.network.NewsClient
import org.example.bigdisplayproject.feature.display.presentation.components.NewsDetails
import org.example.bigdisplayproject.feature.display.presentation.components.NewsList

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App(client: NewsClient) {
    MaterialTheme {




        val store = remember {
            DisplayStoreFactory(storeFactory = DefaultStoreFactory(), client).create()
        }
        val state by store.stateFlow.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Ошибка: ${state.error}")
                state.selectedNews != null -> NewsDetails(state.selectedNews!!)
                else -> NewsList(state.news) { id ->
                    store.accept(DisplayStore.Intent.GetNewsById(id))
                }
            }
        }
    }
}