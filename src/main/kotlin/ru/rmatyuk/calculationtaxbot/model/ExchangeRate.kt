package ru.rmatyuk.calculationtaxbot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
data class ExchangeRate(
        @Id
        @GeneratedValue
        val id: Long? = null,
        @Column(name = "date_rate")
        var dateRate: LocalDate? = null,
        @Column(name = "rate_jpy")
        var rateJpy: Int? = null,
        @Column(name = "rate_rub")
        var rateRub: Int? = null
)
