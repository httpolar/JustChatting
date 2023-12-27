package moe.polar.justchatting.plugins

import com.zaxxer.hikari.HikariDataSource
import moe.polar.justchatting.entities.tables.MessagesTable
import moe.polar.justchatting.entities.tables.PasswordsTable
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.generateDataSource
import moe.polar.justchatting.extensions.hikariProperties
import moe.polar.justchatting.extensions.query
import org.jetbrains.exposed.sql.*

suspend fun setupDatabase(ds: HikariDataSource = generateDataSource(hikariProperties())) {
    Database.connect(ds)

    query {
        SchemaUtils.createMissingTablesAndColumns(
            UsersTable,
            PasswordsTable,
            TokensTable,
            MessagesTable,
        )
    }
}