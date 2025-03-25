package ru.hld.back.backapi.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hld.back.backapi.data.entities.User
import ru.hld.back.backapi.data.services.UserService
import ru.hld.back.backapi.security.TokenUtils
import ru.hld.back.backapi.security.ValidationUtils.isValidPassword
import ru.hld.back.backapi.security.ValidationUtils.isValidUsername
import java.sql.Date

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = ["*"])
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenUtils: TokenUtils
) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<Any> {
        if (!isValidUsername(request.login) || !isValidPassword(request.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password format")
        }

        val user = userService.userByLogin(request.login)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found")

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password")
        }

        val jwtToken = tokenUtils.generateJwt(user)

        return ResponseEntity.ok(mapOf("token" to jwtToken))
    }

    @PostMapping("/register")
    fun addUser(@RequestBody request: UserRegistrationRequest): ResponseEntity<String> {

        when {

            !isValidUsername(request.login)
                    || !isValidPassword(request.password) -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Wrong login or password")
            }

            userService.userByLogin(request.login) != null -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists")
            }

        }

        val passwordEncoded = passwordEncoder.encode(request.password)

        val newUser = User().apply {
            this.login = request.login
            this.password = passwordEncoded
            this.registerDate = Date(System.currentTimeMillis())
        }

        userService.add(newUser)

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
    }

    //DATA SECTION

    data class UserRegistrationRequest(
        val login: String,
        val password: String,
    )

    data class UserLoginRequest(
        val login: String,
        val password: String
    )
}