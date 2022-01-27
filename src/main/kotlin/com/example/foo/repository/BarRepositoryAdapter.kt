package com.example.foo.repository

import com.example.foo.converter.BarConverter
import com.example.foo.jooq.tables.Bar.BAR
import com.example.foo.jooq.tables.records.BarRecord
import com.example.foo.model.Bar
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class BarRepositoryAdapter(
    private val dslContext: DSLContext,
    private val converter: BarConverter
) : BarRepository {
    override fun all(): List<Bar> = dslContext.selectFrom(BAR).fetch(this::toModel)

    private fun toModel(record: Record): Bar =
        converter.convert(record.into(BarRecord::class.java))
}