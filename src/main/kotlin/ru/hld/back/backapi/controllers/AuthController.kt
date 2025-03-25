package ru.hld.back.backapi.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hld.back.backapi.data.entities.User
import ru.hld.back.backapi.data.services.UserService
import ru.hld.back.backapi.security.JWTUtils
import ru.hld.back.backapi.security.ValidationUtils.isValidPassword
import ru.hld.back.backapi.security.ValidationUtils.isValidUsername
import java.sql.Date
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = ["http://localhost:5173"])
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenUtils: JWTUtils
) {

    @PostMapping("/login")
    fun login(@RequestBody request: UserLoginRequest): ResponseEntity<Any> {

        if (!isValidUsername(request.login) || !isValidPassword(request.password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong login or password")
        }

        val user = userService.userByLogin(request.login)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found")

        if (!passwordEncoder.matches(request.password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password")
        }

        return ResponseEntity.ok(tokenUtils.generateAuthTokens(user))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<Any> {

        if (!isValidUsername(request.login) || !isValidPassword(request.password)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid username or password format")
        }

        if (userService.userByLogin(request.login) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists")
        }

        val passwordEncoded = passwordEncoder.encode(request.password)

        val newUser = User().apply {
            this.login = request.login
            this.password = passwordEncoded
            this.registerDate = Date(System.currentTimeMillis())
        }

        userService.add(newUser)

        return ResponseEntity.ok(tokenUtils.generateAuthTokens(newUser))
    }

    @PostMapping("/token")
    fun token(@CookieValue("access_token") accessToken: String, @CookieValue("refresh_token") refreshToken: String): ResponseEntity<Any> {
        if (tokenUtils.validate(accessToken)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Token validated")
        } else if (tokenUtils.validate(refreshToken)) {

            val login = tokenUtils.getLogin(refreshToken)
            val user = userService.userByLogin(login) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found")

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenUtils.generateAuthTokens(user))
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid")
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