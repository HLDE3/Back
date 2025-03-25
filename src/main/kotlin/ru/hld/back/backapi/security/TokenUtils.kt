package ru.hld.back.backapi.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.hld.back.backapi.data.entities.User
import java.sql.Date
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class TokenUtils {

    @Value("\${security.key}")
    private lateinit var key: String

    fun generateJwt(user: User): String {
        val now = Instant.now()
        val expiryDate = now.plus(1, ChronoUnit.MINUTES)

        return Jwts.builder()
            .setSubject(user.login)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiryDate))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact()
    }

}