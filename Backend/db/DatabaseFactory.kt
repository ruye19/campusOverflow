package db

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

object DatabaseFactory {
    fun init(environment: ApplicationEnvironment) {
        val config = environment.config

        val host = config.property("database.host").getString()
        val port = config.property("database.port").getString()
        val dbName = config.property("database.name").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()

        val jdbcURL = "jdbc:mysql://$host:$port/$dbName?useSSL=false&serverTimezone=UTC"

        Database.connect(
            url = jdbcURL,
            driver = "com.mysql.cj.jdbc.Driver",
            user = user,
            password = password
        )

        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }
}
