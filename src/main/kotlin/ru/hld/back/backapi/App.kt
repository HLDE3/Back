package ru.hld.back.backapi

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
class Main

@Configuration
class AppConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173") // Ваш фронтенд-адрес
            .allowCredentials(true)
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }
}

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/**").permitAll()
            }
            .csrf { it.disable() }
            .cors { it.disable() }

        return http.build()
    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}