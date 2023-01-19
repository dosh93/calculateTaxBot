package ru.rmatyuk.calculationtaxbot.controller

import org.springframework.stereotype.Component
import ru.rmatyuk.calculationtaxbot.csvModel.Calculate
import ru.rmatyuk.calculationtaxbot.enums.ConfigType
import ru.rmatyuk.calculationtaxbot.model.User
import ru.rmatyuk.calculationtaxbot.utils.CsvUtils
import java.math.RoundingMode

@Component
class CalculateController(csv: CsvUtils, configController: ConfigController, exchangeRateController: ExchangeRateController) {

    private final val csv: CsvUtils
    private final val configController: ConfigController
    private final val exchangeRateController: ExchangeRateController

    init {
        this.csv = csv
        this.configController = configController
        this.exchangeRateController = exchangeRateController
    }

    fun getEstimatedDutyUsd(user: User): Int {
        val year = user.year!!
        val power = user.power!!
        val powerYearCsv = csv.readPowerYear()
        val powerNdsCsv = csv.readPowerNds()
        var colIndex = 0
        var tollUsd = 0.0
        var tollPercentNsd = 0.0
        var tollPercentPosh = 0.0

        powerYearCsv[0].drop(1).forEachIndexed { index, rangeYear ->
            if (rangeYear != "") {
                val split = rangeYear.split("-")
                println("index=$index rangeYear=$rangeYear split=$split")
                if (split[0] == "") {
                    if (split[1].toInt() <= year) colIndex = index + 1
                } else if (split[1] == "") {
                    if (split[0].toInt() >= year) colIndex = index + 1
                } else {
                    if (split[0].toInt() <= year && split[1].toInt() >= year) colIndex = index + 1
                }
            }
        }

        powerYearCsv.drop(1).forEach { row ->
            if (row[0] != "") {
                val split = row[0].split("-")
                if (split[0] == "") {
                    if (split[1].toInt() >= power) {
                        tollUsd = row[colIndex].replace(",", ".").toDouble()
                    }
                } else if (split[1] == "") {
                    if (split[0].toInt() <= power) {
                        tollUsd = row[colIndex].replace(",", ".").toDouble()
                    }
                } else {
                    if (split[0].toInt() <= power && split[1].toInt() >= power) {
                        tollUsd = row[colIndex].replace(",", ".").toDouble()
                    }
                }
            }
        }

        powerNdsCsv.drop(1).forEach { row ->
            val split = row[0].split("-")
            if (split[0] == "") {
                if (split[1].toInt() >= power) {
                    tollPercentPosh = row[1].toDouble()
                    tollPercentNsd = row[2].toDouble()
                }
            } else if (split[1] == "") {
                if (split[0].toInt() <= power) {
                    tollPercentPosh = row[1].toDouble()
                    tollPercentNsd = row[2].toDouble()
                }
            } else {
                if (split[0].toInt() <= power && split[1].toInt() >= power) {
                    tollPercentPosh = row[1].toDouble()
                    tollPercentNsd = row[2].toDouble()
                }
            }
        }
        val tollUsdWithPosh = tollUsd * (tollPercentPosh / 100)
        val nds = (tollUsdWithPosh + tollUsd) * (tollPercentNsd / 100)
        return (nds + tollUsdWithPosh).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
    }

    fun getCalculate(user: User): Calculate {
        val calculate: Calculate
        val configAll = configController.getAllConfig()
        val rateTotalPriceJpy = configAll[ConfigType.RATE_TOTAL_PRICE_JPY]!!.toInt()
        val rate = exchangeRateController.getRateToday()
        calculate = if (rateTotalPriceJpy > user.bidAuction!!) {
            Calculate(
                    user.bidAuction!!.toInt(),
                    configAll[ConfigType.IMPERIAL_TAX_PERCENT1]!!.toInt(),
                    configAll[ConfigType.DELIVERY_JPY]!!.toInt(),
                    configAll[ConfigType.AUCTION_FEE_JPY1]!!.toInt(),
                    configAll[ConfigType.AGENT_COMMISSION_JPY1]!!.toInt(),
                    configAll[ConfigType.IMPORT_CONTINENT_RUB1]!!.toInt(),
                    configAll[ConfigType.FREIGHT_USD1]!!.toInt(),
                    configAll[ConfigType.BANKING_FEES_PERCENT]!!.toInt(),
                    configAll[ConfigType.SVH_RUB]!!.toInt(),
                    getEstimatedDutyUsd(user),
                    user.horse,
                    configAll[ConfigType.PRICE_ONE_HORSE]!!.toInt(),
                    configAll[ConfigType.COUNT_HORSE_WITH_CALCULATE]!!.toInt(),
                    configAll[ConfigType.DELIVERY_RUS]!!.toInt(),
                    configAll[ConfigType.MARGIN_PERCENT]!!.toInt(),
                    configAll[ConfigType.MIN_MARGIN]!!.toInt(),
                    rate.rateJpy!!,
                    rate.rateRub!!
            )

        } else {
            Calculate(
                    user.bidAuction!!.toInt(),
                    configAll[ConfigType.IMPERIAL_TAX_PERCENT2]!!.toInt(),
                    configAll[ConfigType.DELIVERY_JPY]!!.toInt(),
                    configAll[ConfigType.AUCTION_FEE_JPY2]!!.toInt(),
                    configAll[ConfigType.AGENT_COMMISSION_JPY2]!!.toInt(),
                    configAll[ConfigType.IMPORT_CONTINENT_RUB2]!!.toInt(),
                    configAll[ConfigType.FREIGHT_USD2]!!.toInt(),
                    configAll[ConfigType.BANKING_FEES_PERCENT]!!.toInt(),
                    configAll[ConfigType.SVH_RUB]!!.toInt(),
                    getEstimatedDutyUsd(user),
                    user.horse,
                    configAll[ConfigType.PRICE_ONE_HORSE]!!.toInt(),
                    configAll[ConfigType.COUNT_HORSE_WITH_CALCULATE]!!.toInt(),
                    configAll[ConfigType.DELIVERY_RUS]!!.toInt(),
                    configAll[ConfigType.MARGIN_PERCENT]!!.toInt(),
                    configAll[ConfigType.MIN_MARGIN]!!.toInt(),
                    rate.rateJpy!!,
                    rate.rateRub!!
            )
        }
        return calculate
    }
}