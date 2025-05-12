package com.example.plugins

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import com.example.utils.JWTUtil
import org.slf4j.LoggerFactory

fun Application.configureSecurity() {
    // CORS can be set up here if you need it
    install(Authentication) {
        jwt {
            realm = "ktor.io"
            verifier {
                val secret = JWTUtil.getSecretKey() // Load the secret key from the environment
                Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
            }
            validate { credential ->
                val userId = credential.payload.getClaim("userid").asString()
                if (userId.isNotEmpty()) {
                    UserIdPrincipal(userId)
                } else null
            }
        }
    }
}
