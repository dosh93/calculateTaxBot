package ru.rmatyuk.calculationtaxbot.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.rmatyuk.calculationtaxbot.model.User

@Repository
interface UserRepository : CrudRepository<User, Long>{

}