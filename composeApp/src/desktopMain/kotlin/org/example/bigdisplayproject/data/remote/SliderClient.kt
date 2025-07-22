package org.example.bigdisplayproject.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class SliderClient {
    val httpClient: HttpClient = createSliderHttpClient(CIO)
}