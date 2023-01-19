package ru.rmatyuk.calculationtaxbot.controller

import org.springframework.stereotype.Component
import ru.rmatyuk.calculationtaxbot.enums.ConfigType
import ru.rmatyuk.calculationtaxbot.repository.ConfigRepository

@Component
class ConfigController(configRepository: ConfigRepository) {

    private final val configRepository: ConfigRepository

    init {
        this.configRepository = configRepository
    }

    fun getConfigValue(key: ConfigType): String {
        return configRepository.findById(key).get().value!!
    }

    fun getAllConfig(): Map<ConfigType, String> {
        val result = mutableMapOf<ConfigType, String>()
        configRepository.findAll().forEach { row ->
            result[row.key!!] = row.value!!
        }
        return result
    }
}