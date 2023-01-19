package ru.rmatyuk.calculationtaxbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("application.yml")
data class BotConfig(
        @Value("\${bot.name}")
        val botName: String,
        @Value("\${bot.token}")
        val token: String
)
