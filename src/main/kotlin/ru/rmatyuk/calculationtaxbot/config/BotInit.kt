package ru.rmatyuk.calculationtaxbot.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.rmatyuk.calculationtaxbot.service.TelegramBot

@Component
class BotInit {
    @Autowired
    var bot: TelegramBot? = null

    @EventListener(ContextRefreshedEvent::class)
    @Throws(TelegramApiException::class)
    fun init() {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(bot)
    }
}