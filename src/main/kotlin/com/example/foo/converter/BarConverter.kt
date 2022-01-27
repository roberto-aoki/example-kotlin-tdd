package com.example.foo.converter

import com.example.foo.jooq.tables.records.BarRecord
import com.example.foo.model.Bar
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class BarConverter : Converter<BarRecord, Bar> {
    override fun convert(source: BarRecord): Bar = Bar(
        id = source.id,
        name = source.name
    )
}
