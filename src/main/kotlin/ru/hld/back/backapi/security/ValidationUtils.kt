package ru.hld.back.backapi.security

import java.util.regex.Pattern

object ValidationUtils {

    private val USERNAME_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_.]{4,16}$")

    private val PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-])[\\w!@#$%^&*()_+=-]{8,30}$")

    fun isValidUsername(username: String?): Boolean {
        return when {
            username.isNullOrBlank() || !USERNAME_PATTERN.matcher(username).matches() -> false
            else -> true
        }
    }

    fun isValidPassword(password: String?): Boolean {
        return when {
            password.isNullOrBlank() || !PASSWORD_PATTERN.matcher(password).matches() -> false
            else -> true
        }
    }
}