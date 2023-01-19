package ru.rmatyuk.calculationtaxbot.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import ru.rmatyuk.calculationtaxbot.enums.StateUser

@Entity(name = "tg_users")
data class User(
        @Id
        val chatId: Long? = null,
        var state: StateUser? = null,
        var bidAuction: Double? = null,
        var year: Int? = null,
        var power: Int? = null,
        var horse: Int = 0
)
