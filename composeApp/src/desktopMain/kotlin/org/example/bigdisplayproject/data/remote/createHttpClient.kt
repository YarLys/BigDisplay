package org.example.bigdisplayproject.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.example.bigdisplayproject.domain.entities.news.Album
import org.example.bigdisplayproject.domain.entities.news.Attachment
import org.example.bigdisplayproject.domain.entities.news.Doc
import org.example.bigdisplayproject.domain.entities.news.Link
import org.example.bigdisplayproject.domain.entities.news.Photo
import org.example.bigdisplayproject.domain.entities.news.Poll
import org.example.bigdisplayproject.domain.entities.news.Video

fun createNewsHttpClient(engine: CIO): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                //ignoreUnknownKeys = true
                isLenient = true
                classDiscriminator = "type"  // Поле, по которому определяется тип
                serializersModule = SerializersModule {
                    polymorphic(Attachment::class) {
                        subclass(Photo::class)
                        subclass(Album::class)
                        subclass(Video::class)
                        subclass(Doc::class)
                        subclass(Link::class)
                        subclass(Poll::class)
                    }
                }
            })
        }
    }
}

fun createScheduleHttpClient(engine: CIO): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
}