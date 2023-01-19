package ru.rmatyuk.calculationtaxbot.controller

import org.springframework.stereotype.Component
import ru.rmatyuk.calculationtaxbot.enums.StateUser
import ru.rmatyuk.calculationtaxbot.model.User
import ru.rmatyuk.calculationtaxbot.repository.UserRepository

@Component
class UserController(repository: UserRepository) {
    private final val userRepository: UserRepository

    init {
        userRepository = repository
    }

    fun register(chatId: Long): User {
        val userOpt = userRepository.findById(chatId)
        val user: User
        if (userOpt.isEmpty) {
            user = User(chatId, StateUser.START)
            userRepository.save(user)
        } else {

            user = userOpt.get()
            user.state = StateUser.START
            userRepository.save(user)
        }
        return user
    }

    fun getUser(chatId: Long): User {
        return userRepository.findById(chatId).get()
    }

    fun setState(user: User, stateUser: StateUser): User {
        user.state = stateUser
        userRepository.save(user)
        return user
    }

    fun setBidAuction(user: User, bidAuction: Double) {
        user.bidAuction = bidAuction
        user.state = StateUser.YEAR
        userRepository.save(user)
    }

    fun setYear(user: User, year: Int) {
        user.year = year
        user.state = StateUser.POWER
        userRepository.save(user)
    }

    fun setPower(user: User, power: Int) {
        user.power = power
        user.state = StateUser.HORSE
        userRepository.save(user)
    }

    fun setHorse(user: User, horse: Int): User {
        user.horse = horse
        user.state = StateUser.CALCULATE
        userRepository.save(user)
        return user
    }
}