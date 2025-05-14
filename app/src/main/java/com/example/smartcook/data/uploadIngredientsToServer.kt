package com.example.smartcook.data


import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

suspend fun uploadIngredientsToServer(
    ingredients: List<String>,
    url: String
): String {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    return try {
        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(MatchIngredientsRequest(ingredients))
        }
        response.bodyAsText()
    } finally {
        client.close()
    }
}

@kotlinx.serialization.Serializable
data class MatchIngredientsRequest(
    val ingredients: List<String>
)