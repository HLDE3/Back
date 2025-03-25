package ru.hld.back.backapi.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.hld.back.backapi.data.entities.User
import ru.hld.back.backapi.data.services.UserService

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = ["*"])
class UserController @Autowired constructor(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.users)
    }

    @GetMapping("/search")
    fun getUser(@RequestParam(name = "name") name: String): ResponseEntity<User> {
        val user = userService.userByLogin(name)


        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping
    fun deleteUser(@RequestParam(name = "name") name: String): ResponseEntity<Void> {
        val user = userService.users.firstOrNull { it.login == name }
        return if (user != null) {
            userService.delete(user)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}