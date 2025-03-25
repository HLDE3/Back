package ru.hld.back.backapi.data.services

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import ru.hld.back.backapi.data.entities.User

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository
) {

    val users: MutableList<User> get() = userRepository.findAll()

    val userByLogin: (String) -> User? = userRepository::findByLogin

    val add: (User) -> User = userRepository::save

    val delete: (User) -> Unit = userRepository::delete

}