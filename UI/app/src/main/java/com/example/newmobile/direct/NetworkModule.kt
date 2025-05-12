package com.frontendmasters.app.di

import com.frontendmasters.app.data.remote.CampusOverflowApi
import com.frontendmasters.app.data.remote.CampusOverflowApiImpl
import com.frontendmasters.app.util.Constants
import com.frontendmasters.app.util.NetworkException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 30000L // 30 seconds

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            // Timeout configuration
            engine {
                requestTimeout = TIMEOUT
                maxConnectionsCount = 1000
                endpoint {
                    maxConnectionsPerRoute = 100
                    pipelineMaxSize = 20
                    keepAliveTime = 5000
                    connectTimeout = TIMEOUT
                    connectAttempts = 5
                }
            }

            // Content negotiation for JSON
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }

            // Logging configuration
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }

            // Response observer for debugging
            install(ResponseObserver) {
                onResponse { response ->
                    println("HTTP status: ${response.status.value}")
                    when (response.status.value) {
                        in 200..299 -> Unit // Success
                        in 300..399 -> throw NetworkException(response, "Redirect response received")
                        in 400..499 -> throw NetworkException(response, "Client error: ${response.status.value}")
                        in 500..599 -> throw NetworkException(response, "Server error: ${response.status.value}")
                        else -> throw NetworkException(response, "Unknown error: ${response.status.value}")
                    }
                }
            }

            // Default request configuration
            request {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }

            // Error handling
            expectSuccess = true
        }
    }

    @Provides
    @Singleton
    fun provideCampusOverflowApi(client: HttpClient): CampusOverflowApi {
        return CampusOverflowApiImpl(client, Constants.API_BASE_URL)
    }
} 