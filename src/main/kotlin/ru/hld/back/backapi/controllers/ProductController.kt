package ru.hld.back.backapi.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hld.back.backapi.data.entities.Product
import ru.hld.back.backapi.data.services.ProductService

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = ["http://localhost:5173"])
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(productService.products)
    }

}