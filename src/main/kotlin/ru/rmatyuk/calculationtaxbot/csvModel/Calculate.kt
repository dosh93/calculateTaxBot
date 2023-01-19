package ru.rmatyuk.calculationtaxbot.csvModel

import java.math.RoundingMode

class Calculate(
        bidAuctionJpy: Int,
        imperialTaxPercent: Int,
        deliveryJpy: Int,
        auctionFeeJpy: Int,
        agentCommissionJpy: Int,
        importContinentRub: Int,
        freightUsd: Int,
        bankingFeesPercent: Int,
        svhRub: Int,
        estimatedDutyUsd: Int,
        horse: Int,
        priceOneHorse: Int,
        countHorseWithCalculate: Int,
        deliveryRus: Int,
        marginPercent: Int,
        minMarginRub: Int,
        jpyUsd: Int,
        usdRub: Int
) {
    private val bidAuctionJpy: Int
    private val imperialTaxJpy: Int
    private val deliveryJpy: Int
    private val auctionFeeJpy: Int
    private val agentCommissionJpy: Int
    private val importContinentJpy: Int
    private val freightJpy: Int
    private val freightUsd: Int
    private val bankingFeesUsd: Int
    private val svhUsd: Int
    private val estimatedDutyUsd: Int
    private val priceHorseRub: Int
    private val deliveryRus: Int
    private val marginPercent: Int
    private val minMarginRub: Int
    private val jpyUsd: Int
    private val usdRub: Int


    init {
        this.bidAuctionJpy = bidAuctionJpy
        this.imperialTaxJpy = (this.bidAuctionJpy.toDouble() * (imperialTaxPercent.toDouble() / 100.0)).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
        this.deliveryJpy = deliveryJpy
        this.auctionFeeJpy = auctionFeeJpy
        this.agentCommissionJpy = agentCommissionJpy
        this.importContinentJpy = (importContinentRub.toDouble() / usdRub.toDouble() * jpyUsd.toDouble()).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
        this.freightJpy = freightUsd * jpyUsd
        this.freightUsd = freightUsd
        this.bankingFeesUsd = (
                (((this.bidAuctionJpy + this.imperialTaxJpy
                        + this.deliveryJpy + this.auctionFeeJpy
                        + this.agentCommissionJpy + this.importContinentJpy)
                        / (jpyUsd - 2)).toDouble() + freightUsd.toDouble()) *
                        (bankingFeesPercent.toDouble() / 100.0)).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
        this.svhUsd = (svhRub.toDouble() / usdRub.toDouble()).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt()
        this.estimatedDutyUsd = estimatedDutyUsd
        this.jpyUsd = jpyUsd
        this.usdRub = usdRub
        this.priceHorseRub = if (horse > countHorseWithCalculate) {
            horse * priceOneHorse
        } else {
            0
        }
        this.deliveryRus = deliveryRus
        this.marginPercent = marginPercent
        this.minMarginRub = minMarginRub
    }

    fun getTotalJapanUsd(): Int {
        return ((bidAuctionJpy + imperialTaxJpy + deliveryJpy + auctionFeeJpy + agentCommissionJpy + importContinentJpy).toDouble() / (jpyUsd - 2).toDouble()).toBigDecimal().setScale(0, RoundingMode.HALF_UP).toInt() + freightUsd + bankingFeesUsd
    }

    fun getTotalRussinUsd(): Int {
        val totalWithoutMargin = svhUsd + estimatedDutyUsd + (priceHorseRub / usdRub) + (deliveryRus / usdRub)
        val margin = totalWithoutMargin * (marginPercent / 100)
        return if (margin < (minMarginRub / usdRub)) {
            totalWithoutMargin + (minMarginRub / usdRub)
        } else {
            totalWithoutMargin +  margin
        }
    }

    fun getTotalJapanRub(): Int {
        return getTotalJapanUsd() * usdRub
    }

    fun getTotalRussinRub(): Int {
        return getTotalRussinUsd() * usdRub
    }


    fun getTotalRub(): Int {
        return getTotalJapanRub() + getTotalRussinRub()
    }

    fun getRateJpyUsd(): Int{
        return jpyUsd
    }

    fun getRateUsdRub(): Int{
        return usdRub
    }

}