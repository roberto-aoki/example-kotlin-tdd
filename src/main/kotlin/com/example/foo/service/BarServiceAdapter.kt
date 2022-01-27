package com.example.foo.service

import com.example.foo.model.Bar
import com.example.foo.repository.BarRepository
import org.springframework.stereotype.Service

@Service
class BarServiceAdapter(private val repository: BarRepository) : BarService {
    override fun getList(): List<Bar> = repository.all()
}