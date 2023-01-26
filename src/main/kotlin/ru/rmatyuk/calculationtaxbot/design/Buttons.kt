package ru.rmatyuk.calculationtaxbot.design

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.rmatyuk.calculationtaxbot.enums.CallbackButton

class Buttons {
    companion object {
        @JvmStatic
        fun start(): InlineKeyboardButton {
            return createButton("Поехали! \uD83C\uDFCD", CallbackButton.START.name)
        }

        @JvmStatic
        fun help(): InlineKeyboardButton {
            return createButton("Помощь менеджера \uD83D\uDEA8", "", "https://t.me/paul375")
        }

        @JvmStatic
        fun whatRate(): InlineKeyboardButton {
            return createButton("Где взять ставку? \uD83E\uDD37\uD83C\uDFFC\u200D♀️", CallbackButton.WHAT_RATE.name)
        }

        @JvmStatic
        fun resume(): InlineKeyboardButton {
            return createButton("Продолжить считать ▶️", CallbackButton.RESUME.name)
        }

        @JvmStatic
        fun createButton(text: String, callbackData: String, url: String? = null): InlineKeyboardButton {
            val button = InlineKeyboardButton()
            button.text = text
            button.callbackData = callbackData
            if (url != null) {
                button.url = url
            }
            return button
        }

        @JvmStatic
        fun no(): InlineKeyboardButton {
            return createButton("Нет ❌", CallbackButton.NO.name)
        }

        @JvmStatic
        fun yes(): InlineKeyboardButton {
            return createButton("Да ✅", CallbackButton.YES.name)
        }

        @JvmStatic
        fun whatExciseDuty(): InlineKeyboardButton {
            return createButton("что такое акциз? \uD83E\uDD37\uD83C\uDFFC\u200D♀️", CallbackButton.WHAT_EXCISE_DUTY.name)
        }

        @JvmStatic
        fun vk(): InlineKeyboardButton {
            return createButton("Наше сообщество в ВК \uD83D\uDD35", "","https://vk.com/motojapancom")
        }

        @JvmStatic
        fun tg(): InlineKeyboardButton {
            return createButton("Telegram \uD83D\uDCF2", "","T.me/paul375")
        }

        @JvmStatic
        fun wa(): InlineKeyboardButton {
            return createButton("WhatsApp \uD83D\uDCAC", "","Wa.me/79774776042")
        }

        @JvmStatic
        fun avito(): InlineKeyboardButton {
            return createButton("Отзывы на авито \uD83D\uDFE2", "", "https://www.avito.ru/user/01c57bef5e80d0167670eb9580f9423b/profile?src=sharing")
        }

        @JvmStatic
        fun recount(): InlineKeyboardButton {
            return createButton("Пересчитать \uD83D\uDD04", CallbackButton.RESUME.name)
        }

    }
}