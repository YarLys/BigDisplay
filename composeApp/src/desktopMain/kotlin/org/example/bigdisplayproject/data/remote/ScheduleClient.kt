package org.example.bigdisplayproject.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

class ScheduleClient {
    val httpClient: HttpClient = createScheduleHttpClient(CIO)
}