package com.example.foo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FooApplication

fun main(args: Array<String>) {
    runApplication<FooApplication>(*args)
}
