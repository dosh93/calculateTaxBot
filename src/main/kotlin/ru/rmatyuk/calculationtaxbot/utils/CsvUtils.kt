package ru.rmatyuk.calculationtaxbot.utils

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Component
class CsvUtils() {

    fun readPowerYear(): List<List<String>> {
        val file = load("csv/bid_year.csv")
        return csvReader { delimiter = ';' }.readAll(file)
    }

    fun readPowerNds(): List<List<String>> {
        val file = load("csv/bid_percent.csv")
        return csvReader { delimiter = ';' }.readAll(file)
    }

    fun load(path: String): String {
        val classPathResource = ClassPathResource(path)

        val inputStream = classPathResource.inputStream
        val tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".csv")
        try {
            FileUtils.copyInputStreamToFile(inputStream, tmpFile)
        } finally {
            IOUtils.close(inputStream)
        }
        return tmpFile.readText(charset = Charsets.UTF_8)
    }
}