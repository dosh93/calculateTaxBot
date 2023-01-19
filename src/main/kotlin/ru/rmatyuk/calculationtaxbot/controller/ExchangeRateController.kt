package ru.rmatyuk.calculationtaxbot.controller

import kotlinx.serialization.json.*
import org.springframework.stereotype.Component
import ru.rmatyuk.calculationtaxbot.config.FixerConfig
import ru.rmatyuk.calculationtaxbot.model.ExchangeRate
import ru.rmatyuk.calculationtaxbot.repository.ExchangeRateRepository
import java.math.RoundingMode
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate

@Component
class ExchangeRateController(exchangeRateRepository: ExchangeRateRepository, fixerConfig: FixerConfig) {

    private final val exchangeRateRepository: ExchangeRateRepository
    private final val fixerConfig: FixerConfig

    init {
        this.exchangeRateRepository = exchangeRateRepository
        this.fixerConfig = fixerConfig
    }

    fun getRateToday(): ExchangeRate {
        val toDay = LocalDate.now()
        val exchangeRateList = exchangeRateRepository.getByDateRate(toDay)
        if (exchangeRateList.isEmpty()) {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                    .uri(URI.create(fixerConfig.url))
                    .header("apikey", fixerConfig.token)
                    .GET()
                    .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val decodeFromString = Json.decodeFromString(ExchangeRateResponse.serializer(), response.body())
            val exchangeRate = ExchangeRate()
            exchangeRate.dateRate = toDay
            exchangeRate.rateJpy = decodeFromString.rates["JPY"]!!.toBigDecimal().setScale(0, RoundingMode.CEILING).toInt()
            exchangeRate.rateRub = decodeFromString.rates["RUB"]!!.toBigDecimal().setScale(0, RoundingMode.CEILING).toInt()
            exchangeRateRepository.save(exchangeRate)
            return exchangeRate
        } else {
            return exchangeRateList.first()
        }
    }


}

@kotlinx.serialization.Serializable
data class ExchangeRateResponse(
        val success: Boolean,
        val timestamp: Long,
        val base: String,
        val date: String,
        val rates: Map<String, Double>
)