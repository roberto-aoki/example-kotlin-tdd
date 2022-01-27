package com.example.foo.factory

import com.example.foo.model.Bar
import java.util.UUID

class BarFactory {

    companion object {
        fun createValid(): Bar = Bar(
            id = UUID.randomUUID(),
            name = "Bar 1"
        )
    }
}