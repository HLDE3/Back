package ru.hld.back.backapi.data.services

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import ru.hld.back.backapi.data.entities.Product

@Service
class ProductService @Autowired constructor(
    private val productRepository: ProductRepository
) {

    val products: MutableList<Product> get() = productRepository.findAll()

    val add: (Product) -> Product = productRepository::save

    val delete: (Product) -> Unit = productRepository::delete

}