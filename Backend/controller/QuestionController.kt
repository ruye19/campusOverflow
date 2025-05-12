package controllers

import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.request.receive
import io.ktor.routing.*
import db.DatabaseFactory
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.kotlinx.json
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Question(
    val questionId: String,
    val title: String,
    val description: String,
    val tag: String?,
    val username: String
)

suspend fun createQuestion(call: ApplicationCall) {
    val params = call.receive<Map<String, String>>()
    val title = params["title"]
    val description = params["description"]
    val tag = params["tag"]
    val userId = params["userid"]

    if (title.isNullOrEmpty() || description.isNullOrEmpty()) {
        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Please provide all required fields"))
        return
    }

    try {
        val questionId = UUID.randomUUID().toString() // Create a new question ID
        DatabaseFactory.dbQuery {
            // Insert question into database
            val query = "INSERT INTO questions (title, description, questionid, userid, tag) VALUES (?, ?, ?, ?, ?)"
            // db.execute(query, title, description, questionId, userId, tag)
        }

        call.respond(HttpStatusCode.Created, mapOf("message" to "Question posted successfully", "questionId" to questionId))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "An unexpected error occurred"))
    }
}

// Similar implementations can be done for `getAllQuestion`, `singleQuestion`, `getSeachedQuestion`, etc.
