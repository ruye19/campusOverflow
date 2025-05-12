package controllers

import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.request.receive
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import db.DatabaseFactory
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.kotlinx.json
import kotlinx.serialization.Serializable

// Answer model for the response
@Serializable
data class Answer(val answerId: String, val userId: String, val questionId: String, val answer: String, val username: String)

suspend fun postAnswer(call: ApplicationCall) {
    val params = call.receive<Map<String, String>>()
    val answer = params["answer"]
    val questionId = params["questionid"]
    val userId = params["userid"]

    if (answer == null) {
        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Please provide answer"))
        return
    }

    try {
        DatabaseFactory.dbQuery {
            // Insert answer into the database
            val query = "INSERT INTO answers (userid, questionid, answer) VALUES (?, ?, ?)"
            // db.execute(query, userId, questionId, answer)
        }
        call.respond(HttpStatusCode.Created, mapOf("message" to "Answer posted successfully"))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "An unexpected error occurred."))
    }
}

suspend fun getAnswer(call: ApplicationCall) {
    val questionId = call.parameters["questionid"]

    try {
        val answers = DatabaseFactory.dbQuery {
            // Execute query and retrieve answers
            val query = """
                SELECT a.answerid, a.userid, a.questionid, a.answer, u.username 
                FROM answers a 
                JOIN users u ON a.userid = u.userid 
                WHERE questionid = ?
            """
            // Execute query with questionId
            // val result = db.query(query, questionId)
            // Return result as a list of Answer
        }
        call.respond(HttpStatusCode.OK, mapOf("message" to "Answers retrieved successfully", "answers" to answers))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "An unexpected error occurred."))
    }
}

suspend fun getAnswerStats(call: ApplicationCall) {
    try {
        val totalAnswers = DatabaseFactory.dbQuery {
            // Execute query to count total answers
            val query = "SELECT COUNT(*) AS totalAnswers FROM answers"
            // db.query(query)
            // Return total answers count
        }

        call.respond(HttpStatusCode.OK, mapOf("totalAnswers" to totalAnswers))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Error fetching answer count"))
    }
}
