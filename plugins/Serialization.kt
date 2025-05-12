package com.example.plugins

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson { 
            // You can customize Jackson settings here if needed
            // Example: enable/disable features, custom serializers, etc.
        }
    }
}

