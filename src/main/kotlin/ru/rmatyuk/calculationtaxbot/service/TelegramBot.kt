package ru.rmatyuk.calculationtaxbot.service

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.rmatyuk.calculationtaxbot.config.BotConfig
import ru.rmatyuk.calculationtaxbot.controller.CalculateController
import ru.rmatyuk.calculationtaxbot.controller.ExchangeRateController
import ru.rmatyuk.calculationtaxbot.controller.UserController
import ru.rmatyuk.calculationtaxbot.csvModel.Calculate
import ru.rmatyuk.calculationtaxbot.design.Buttons
import ru.rmatyuk.calculationtaxbot.design.MessageText
import ru.rmatyuk.calculationtaxbot.enums.CallbackButton
import ru.rmatyuk.calculationtaxbot.enums.StateUser
import ru.rmatyuk.calculationtaxbot.model.User
import java.util.Scanner
import kotlin.concurrent.thread

@Component
class TelegramBot(config: BotConfig, userController: UserController, calculateController: CalculateController,
                  exchangeRateController: ExchangeRateController) : TelegramLongPollingBot() {

    private final val token: String
    private final val botUserName: String
    private final val userController: UserController
    private final val calculateController: CalculateController
    private final val exchangeRateController: ExchangeRateController

    init {
        token = config.token
        botUserName = config.botName
        this.userController = userController
        this.calculateController = calculateController
        this.exchangeRateController = exchangeRateController
    }

    override fun getBotToken(): String {
        return token
    }

    override fun getBotUsername(): String {
        return botUserName
    }

    override fun onUpdateReceived(update: Update?) {
        thread {
            if (update != null && update.hasMessage() && update.message.hasText()) {
                val message = update.message
                val replyKeyboardMarkup = ReplyKeyboardMarkup()
                replyKeyboardMarkup.resizeKeyboard = true
                replyKeyboardMarkup.oneTimeKeyboard = true

                if (message.text == "/start") {
                    val inlineKeyboardMarkup = InlineKeyboardMarkup()
                    userController.register(message.chatId)
                    inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.start()))
                    sendMessage(message.chatId, MessageText.startMessage, inlineKeyboardMarkup)

                } else {
                    val user = userController.getUser(message.chatId)
                    val userState = user.state!!
                    if (userState == StateUser.BID_AUCTION) {
                        processBidAuction(message, user)
                    } else if (userState == StateUser.YEAR) {
                        processYear(message, user)
                    } else if (userState == StateUser.POWER) {
                        processPower(message, user)
                    } else if (userState == StateUser.HOW_MACH_HORSE) {
                        processHorse(message, user)
                    } else if (userState == StateUser.HORSE) {
                        processChoseHorse(message, user)
                    }
                }
            } else if (update != null && update.hasCallbackQuery()) {
                val callback = CallbackButton.valueOf(update.callbackQuery.data)
                val chatId = update.callbackQuery.message.chatId
                var user = userController.getUser(chatId)
                val inlineKeyboardMarkup = InlineKeyboardMarkup()
                when (callback) {
                    CallbackButton.START, CallbackButton.RESUME -> {
                        userController.setState(user, StateUser.BID_AUCTION)
                        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatRate()), listOf(Buttons.help()))
                        sendMessage(chatId, MessageText.bidAuction, inlineKeyboardMarkup)
                    }

                    CallbackButton.WHAT_RATE -> {
                        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.resume()), listOf(Buttons.help()))
                        sendMessage(chatId, MessageText.whatRate, inlineKeyboardMarkup)
                    }

                    CallbackButton.YES -> {
                        userController.setState(user, StateUser.HOW_MACH_HORSE)
                        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatExciseDuty()), listOf(Buttons.help()))
                        sendMessage(chatId, MessageText.howManyHorse, inlineKeyboardMarkup)
                    }

                    CallbackButton.NO -> {
                        user = userController.setHorse(user, 0)
                        inlineKeyboardMarkup.keyboard = getKeyboardFinal()
                        sendMessage(chatId, getMessageCalculate(user, calculateController.getCalculate(user)), inlineKeyboardMarkup)
                    }

                    CallbackButton.WHAT_EXCISE_DUTY -> {
                        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatExciseDuty()), listOf(Buttons.help()))
                        sendMessage(chatId, "${MessageText.exciseDuty}\n\n${MessageText.howManyHorse}", inlineKeyboardMarkup)
                    }
                }
            }
        }
    }

    private fun processBidAuction(message: Message, user: User) {
        val bidAuction = message.text.toDoubleOrNull()
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val messageText: String = if (bidAuction == null) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatRate()), listOf(Buttons.help()))
            MessageText.bidAuction
        } else if (bidAuction <= 0) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatRate()), listOf(Buttons.help()))
            MessageText.bidAuction
        } else {
            userController.setBidAuction(user, bidAuction)
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.help()))
            MessageText.year
        }
        sendMessage(message.chatId, messageText, inlineKeyboardMarkup)
    }

    private fun processYear(message: Message, user: User) {
        val year = message.text.toIntOrNull()
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.help()))
        val messageText: String = if (year == null) {
            MessageText.year
        } else if (year <= 0) {
            MessageText.year
        } else {
            userController.setYear(user, year)
            MessageText.power
        }
        sendMessage(message.chatId, messageText, inlineKeyboardMarkup)
    }

    private fun processPower(message: Message, user: User) {
        val power = message.text.toIntOrNull()
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val messageText: String = if (power == null) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.help()))
            MessageText.power
        } else if (power <= 0) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.help()))
            MessageText.power
        } else {
            userController.setPower(user, power)
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.yes(), Buttons.no()), listOf(Buttons.help()))
            MessageText.horse
        }
        sendMessage(message.chatId, messageText, inlineKeyboardMarkup)
    }

    private fun processHorse(message: Message, user: User) {
        val horse = message.text.toIntOrNull()
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val messageText: String = if (horse == null) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatExciseDuty()), listOf(Buttons.help()))
            MessageText.howManyHorse
        } else if (horse <= 0) {
            inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.whatExciseDuty()), listOf(Buttons.help()))
            MessageText.howManyHorse
        } else {
            val user = userController.setHorse(user, horse)
            inlineKeyboardMarkup.keyboard = getKeyboardFinal()
            getMessageCalculate(user, calculateController.getCalculate(user))
        }
        sendMessage(message.chatId, messageText, inlineKeyboardMarkup)
    }

    private fun processChoseHorse(message: Message, user: User) {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = listOf(listOf(Buttons.yes(), Buttons.no()), listOf(Buttons.help()))
        val messageText = MessageText.needPressButton + MessageText.horse
        sendMessage(message.chatId, messageText, inlineKeyboardMarkup)
    }

    fun getMessageCalculate(user: User, calculate: Calculate): String {
        return MessageText.calculate.format(calculate.getTotalRub(), user.year, user.power, user.bidAuction!!.toInt(), calculate.getRateUsdRub(), calculate.getRateJpyUsd())
    }

    fun sendMessage(chatId: Long, message: String, keyboard: ReplyKeyboard? = null) {
        val messageSend = SendMessage()
        messageSend.enableHtml(true)
        messageSend.chatId = chatId.toString()
        messageSend.text = message
        if (keyboard != null) messageSend.replyMarkup = keyboard
        execute(messageSend)
    }

    private fun getKeyboardFinal(): List<List<InlineKeyboardButton>> {
        return listOf(listOf(Buttons.tg(), Buttons.wa()), listOf(Buttons.vk()), listOf(Buttons.avito()), listOf(Buttons.recount()))
    }
}