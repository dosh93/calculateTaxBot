package ru.rmatyuk.calculationtaxbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.rmatyuk.calculationtaxbot.model.ExchangeRate
import java.time.LocalDate

@Repository
interface ExchangeRateRepository : CrudRepository<ExchangeRate, Long> {

    fun getByDateRate(dateRate: LocalDate): List<ExchangeRate>
}