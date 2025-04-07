package ru.hld.back.backapi.data.services

import org.springframework.data.jpa.repository.JpaRepository
import ru.hld.back.backapi.data.entities.Product
import ru.hld.back.backapi.data.entities.User

interface ProductRepository : JpaRepository<Product, Long> {

}