package ru.rmatyuk.calculationtaxbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("application.yml")
data class FixerConfig(
        @Value("\${fixer.url}")
        val url: String,
        @Value("\${fixer.token}")
        val token: String
)
