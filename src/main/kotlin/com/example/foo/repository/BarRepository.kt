package com.example.foo.repository

import com.example.foo.model.Bar

interface BarRepository {
    fun all(): List<Bar>

}
