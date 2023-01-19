package ru.rmatyuk.calculationtaxbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CalculationTaxBotApplication

fun main(args: Array<String>) {
	runApplication<CalculationTaxBotApplication>(*args)
}
