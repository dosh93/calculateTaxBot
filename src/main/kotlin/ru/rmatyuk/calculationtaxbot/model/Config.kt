package ru.rmatyuk.calculationtaxbot.model

import jakarta.persistence.*
import ru.rmatyuk.calculationtaxbot.enums.ConfigType

@Entity(name = "bot_config")
data class Config(
        @Id
        @Enumerated(EnumType.STRING)
        @Column(name="key_config")
        val key: ConfigType? = null,
        @Column(name="value_config")
        val value: String? = null,
        @Column(name="description_config")
        val description: String? = null
)
