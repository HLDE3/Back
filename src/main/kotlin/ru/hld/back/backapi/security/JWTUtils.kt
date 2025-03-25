package ru.hld.back.backapi.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.hld.back.backapi.data.entities.User
import java.security.SignatureException
import java.sql.Date
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class JWTUtils {

    @Value("\${security.key}")
    private lateinit var key: String

    fun generate(user: User, duration: Long, unit: ChronoUnit): String {
        val now = Instant.now()
        val expiryDate = now.plus(duration, unit)

        return Jwts.builder()
            .setSubject(user.login)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiryDate))
            .signWith(SignatureAlgorithm.HS512, key)
            .claim("preferred_login", user.login)
            .compact()
    }

    fun generateAuthTokens(user: User): Map<String, String> {
        val accessToken = generate(user, 5, ChronoUnit.MINUTES)
        val refreshToken = generate(user, 30, ChronoUnit.DAYS)

        return mapOf("access_token" to accessToken, "refresh_token" to refreshToken)
    }

    fun getLogin(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }

    fun validate(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (ex: SignatureException) {
            // Invalid JWT signature
            false
        } catch (ex: MalformedJwtException) {
            // Invalid JWT token
            false
        } catch (ex: ExpiredJwtException) {
            // Expired JWT token
            false
        } catch (ex: UnsupportedJwtException) {
            // Unsupported JWT token
            false
        } catch (ex: IllegalArgumentException) {
            // JWT claims string is empty
            false
        }
    }

}