package ru.rmatyuk.calculationtaxbot.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class CsvUtils(resourceLoader: ResourceLoader) {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    init {
        this.resourceLoader = resourceLoader
    }

    fun readPowerYear(): List<List<String>> {
        val file = load("csv/bid_year.csv")
        return csvReader { delimiter = ';' }.readAll(file)
    }

    fun readPowerNds(): List<List<String>> {
        val file = load("csv/bid_percent.csv")
        return csvReader { delimiter = ';' }.readAll(file)
    }

    fun load(path: String): String {
        return resourceLoader.getResource("classpath:$path").file
                .readText(charset = Charsets.UTF_8)
    }
}