package moe.polar.justchatting.extensions

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.File
import java.util.Properties
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    context: CoroutineContext? = Dispatchers.IO,
    db: Database? = null,
    transactionIsolation: Int? = null,
    statement: suspend Transaction.() -> T
): T {
    return newSuspendedTransaction(context, db, transactionIsolation, statement)
}

fun hikariProperties(filename: String = "hikari.properties"): Properties {
    val props = Properties()
    val file = File(filename)

    if (file.exists()) {
        props.load(file.reader())
    } else {
        props.apply {
            setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
            setProperty("dataSource.user", "username")
            setProperty("dataSource.password", "password")
            setProperty("dataSource.databaseName", "justchatting")
            setProperty("dataSource.portNumber", "5432")
            setProperty("dataSource.serverName", "127.0.0.1")
        }

        file.writer().use {
            props.store(it, "Just Chatting API")
            it.close()
        }

        error("HikariCP properties file didn't exist so it was created, please fill the configuration values in.")
    }

    return props
}

fun generateDataSource(properties: Properties): HikariDataSource {
    return HikariDataSource(HikariConfig(properties))
}