package ru.hld.back.backapi.security

import java.util.regex.Pattern

object ValidationUtils {

    private val USERNAME_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_.]{4,16}$")

    private val PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-])[\\w!@#$%^&*()_+=-]{8,30}$")

    fun isValidUsername(username: String?): Boolean {

        if (username.isNullOrBlank()) {
            return false
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return false
        }

        return true
    }

    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) {
            return false
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false
        }

        return true
    }
}