package com.yuan.tsdtinterview.network

import android.content.Context
import android.content.ServiceConnection
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.fullPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object MyClient {
    private var serviceConnection: ServiceConnection? = null

    fun initialize(context: Context) {
        Log.d("MyClient", "MyClient started")
    }

    fun cleanup(context: Context) {
        serviceConnection?.let {
            context.unbindService(it)
            serviceConnection = null
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    val serializeJson = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    val httpClient = HttpClient{
        install(ContentNegotiation){
            json(json = serializeJson)
        }

        install(ResponseObserver){
            onResponse { response ->
                val statusCode = response.status.value
                Log.d("MyClient","⚠️response status code: $statusCode")
            }
        }

        expectSuccess = false

        install(HttpRequestRetry){
            retryOnServerErrors(2)
            exponentialDelay()
        }

        defaultRequest {
            headers{
                append(HttpHeaders.Accept, "application/json")
            }
        }
    }
}