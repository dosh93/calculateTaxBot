package ru.rmatyuk.calculationtaxbot.config

import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import ru.rmatyuk.calculationtaxbot.enums.ConfigType
import ru.rmatyuk.calculationtaxbot.model.Config
import ru.rmatyuk.calculationtaxbot.repository.ConfigRepository

@Component
class OnApplicationStartUp(configRepository: ConfigRepository) {

    private final val configRepository: ConfigRepository

    init {
        this.configRepository = configRepository
    }

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent?) {
        val allConfig = mutableListOf<Config>()
        if (configRepository.findById(ConfigType.RATE_TOTAL_PRICE_JPY).isEmpty) {
            allConfig.add(Config(ConfigType.RATE_TOTAL_PRICE_JPY, "520000", "Больше какой суммы повышаются ставки"))
        }
        if (configRepository.findById(ConfigType.IMPERIAL_TAX_PERCENT1).isEmpty) {
            allConfig.add(Config(ConfigType.IMPERIAL_TAX_PERCENT1, "5", "Потребительский налог (Императорский налог) до RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.IMPERIAL_TAX_PERCENT2).isEmpty) {
            allConfig.add(Config(ConfigType.IMPERIAL_TAX_PERCENT2, "10", "Потребительский налог (Императорский налог) после RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.DELIVERY_JPY).isEmpty) {
            allConfig.add(Config(ConfigType.DELIVERY_JPY, "36000", "Доставка по Японии"))
        }
        if (configRepository.findById(ConfigType.AUCTION_FEE_JPY1).isEmpty) {
            allConfig.add(Config(ConfigType.AUCTION_FEE_JPY1, "13000", "Аукционный сбор до RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.AUCTION_FEE_JPY2).isEmpty) {
            allConfig.add(Config(ConfigType.AUCTION_FEE_JPY2, "20000", "Аукционный сбор после RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.AGENT_COMMISSION_JPY1).isEmpty) {
            allConfig.add(Config(ConfigType.AGENT_COMMISSION_JPY1, "25000", "Комиссия Агента/Экспортера до RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.AGENT_COMMISSION_JPY2).isEmpty) {
            allConfig.add(Config(ConfigType.AGENT_COMMISSION_JPY2, "65000", "Комиссия Агента/Экспортера после RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.IMPORT_CONTINENT_RUB1).isEmpty) {
            allConfig.add(Config(ConfigType.IMPORT_CONTINENT_RUB1, "20000", "Комиссия за приобретение Импорт Континент до RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.IMPORT_CONTINENT_RUB2).isEmpty) {
            allConfig.add(Config(ConfigType.IMPORT_CONTINENT_RUB2, "25000", "Комиссия за приобретение Импорт Континент после RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.FREIGHT_USD1).isEmpty) {
            allConfig.add(Config(ConfigType.FREIGHT_USD1, "400", "Фрахт, Стивидорные услуги до RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.FREIGHT_USD2).isEmpty) {
            allConfig.add(Config(ConfigType.FREIGHT_USD2, "650", "Фрахт, Стивидорные услуги после RATE_TOTAL_PRICE_JPY"))
        }
        if (configRepository.findById(ConfigType.BANKING_FEES_PERCENT).isEmpty) {
            allConfig.add(Config(ConfigType.BANKING_FEES_PERCENT, "3", "Банковские комиссии  и услуги %"))
        }
        if (configRepository.findById(ConfigType.SVH_RUB).isEmpty) {
            allConfig.add(Config(ConfigType.SVH_RUB, "30000", "Расходы по таможенному оф-нию/ СВХ/сбгтс"))
        }
        if (configRepository.findById(ConfigType.PRICE_ONE_HORSE).isEmpty) {
            allConfig.add(Config(ConfigType.PRICE_ONE_HORSE, "554", "Цена за одну лошадь если лошадей больше чем COUNT_HORSE_WITH_CALCULATE"))
        }
        if (configRepository.findById(ConfigType.COUNT_HORSE_WITH_CALCULATE).isEmpty) {
            allConfig.add(Config(ConfigType.COUNT_HORSE_WITH_CALCULATE, "150", "Количество лошадей после которого считается акциза"))
        }
        if (configRepository.findById(ConfigType.DELIVERY_RUS).isEmpty) {
            allConfig.add(Config(ConfigType.DELIVERY_RUS, "30000", "Доставка по России"))
        }
        if (configRepository.findById(ConfigType.MARGIN_PERCENT).isEmpty) {
            allConfig.add(Config(ConfigType.MARGIN_PERCENT, "15", "Маржа от общего"))
        }
        if (configRepository.findById(ConfigType.MIN_MARGIN).isEmpty) {
            allConfig.add(Config(ConfigType.MIN_MARGIN, "50000", "Минимальная маржа"))
        }
        configRepository.saveAll(allConfig)
    }
}