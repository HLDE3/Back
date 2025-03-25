package ru.hld.back.backapi.data.entities

import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "users")
open class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "ban", nullable = false)
    open var ban: Boolean = false

    @Column(name = "login", nullable = false)
    open lateinit var login: String

    @Column(name = "password", nullable = false)
    open lateinit var password: String

    @Column(name = "register_date", nullable = false)
    open lateinit var registerDate: Date

}