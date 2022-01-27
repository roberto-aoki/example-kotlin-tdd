package com.example.foo.controller

import com.example.foo.model.Bar
import com.example.foo.service.BarService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bar")
class BarController(private val service: BarService) {

    @GetMapping
    fun getList(): List<Bar> = service.getList()
}
