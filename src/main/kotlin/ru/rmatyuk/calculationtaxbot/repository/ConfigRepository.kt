package ru.rmatyuk.calculationtaxbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.rmatyuk.calculationtaxbot.enums.ConfigType
import ru.rmatyuk.calculationtaxbot.model.Config

@Repository
interface ConfigRepository: CrudRepository<Config, ConfigType> {
}