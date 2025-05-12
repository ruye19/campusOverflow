package com.example.controllers

import io.ktor.application.*
import io.ktor.response.respond
import io.ktor.request.receive
import io.ktor.http.HttpStatusCode
import com.example.models.UserDTO
import com.example.models.LoginDTO
import com.example.database.DBConnection
import com.example.utils.JWTUtil
import io.ktor.auth.UserIdPrincipal
import com.example.database.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.auth.authenticate

object UserController {

    suspend fun register(call: ApplicationCall, user: UserDTO) {
        // Retrieve user details from the request body
        val userDetails = user

        // Insert the user into the database
        transaction {
            try {
                val userId = UserTable
                    .insertAndGetId {
                        it[username] = userDetails.username
                        it[email] = userDetails.email
                        it[password] = userDetails.password // Store hashed password, never plain text
                        it[firstname] = userDetails.firstname
                        it[lastname] = userDetails.lastname
                        it[profession] = userDetails.profession
                    }
                call.respond(HttpStatusCode.Created, "User successfully created!")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error creating user: ${e.message}")
            }
        }
    }

    suspend fun login(call: ApplicationCall, user: LoginDTO) {
        val loginDetails = user

        transaction {
            // Fetch user from DB based on email
            val userRecord = UserTable
                .select { UserTable.email eq loginDetails.email }
                .singleOrNull()

            if (userRecord != null) {
                val dbPassword = userRecord[UserTable.password]
                if (dbPassword == loginDetails.password) { // Ideally, use password hashing comparison
                    // Generate JWT Token
                    val token = JWTUtil.generateToken(userRecord[UserTable.username])
                    call.respond(HttpStatusCode.OK, mapOf("token" to token))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "User not found")
            }
        }
    }

    suspend fun checkUser(call: ApplicationCall) {
        // JWT Authentication is assumed to be set up
        val principal = call.principal<UserIdPrincipal>()
        if (principal != null) {
            call.respond(HttpStatusCode.OK, "User is authenticated: ${principal.name}")
        } else {
            call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
        }
    }

    suspend fun getFullName(call: ApplicationCall) {
        val principal = call.principal<UserIdPrincipal>()
        if (principal != null) {
            transaction {
                val userRecord = UserTable
                    .select { UserTable.username eq principal.name }
                    .singleOrNull()

                if (userRecord != null) {
                    val fullName = "${userRecord[UserTable.firstname]} ${userRecord[UserTable.lastname]}"
                    call.respond(HttpStatusCode.OK, fullName)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
        }
    }

    suspend fun getUserStats(call: ApplicationCall) {
        val principal = call.principal<UserIdPrincipal>()
        if (principal != null) {
            transaction {
                val userRecord = UserTable
                    .select { UserTable.username eq principal.name }
                    .singleOrNull()

                if (userRecord != null) {
                    val userStats = mapOf(
                        "username" to userRecord[UserTable.username],
                        "email" to userRecord[UserTable.email],
                        "profession" to userRecord[UserTable.profession]
                    )
                    call.respond(HttpStatusCode.OK, userStats)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        } else {
            call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
        }
    }

    suspend fun getAllUserNamesAndProfessions(call: ApplicationCall) {
        transaction {
            val users = UserTable
                .selectAll()
                .map {
                    mapOf(
                        "username" to it[UserTable.username],
                        "profession" to it[UserTable.profession]
                    )
                }
            call.respond(HttpStatusCode.OK, users)
        }
    }
}
