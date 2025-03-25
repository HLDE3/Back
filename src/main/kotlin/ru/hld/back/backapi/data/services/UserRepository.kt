package ru.hld.back.backapi.data.services

import org.springframework.data.jpa.repository.JpaRepository
import ru.hld.back.backapi.data.entities.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByLogin(login: String): User?

}