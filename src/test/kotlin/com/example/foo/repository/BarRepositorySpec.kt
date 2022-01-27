package com.example.foo.repository

import com.example.foo.converter.BarConverter
import com.example.foo.exception.BarException
import com.example.foo.factory.BarFactory
import com.example.foo.jooq.tables.Bar.BAR
import com.example.foo.jooq.tables.records.BarRecord
import com.example.foo.model.Bar
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.jooq.DSLContext
import org.jooq.RecordMapper

class BarRepositorySpec: BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    Given("um service que manipula o bar") {
        val dsl: DSLContext = mockk()
        val converter: BarConverter = mockk()
        val repository: BarRepository = spyk(BarRepositoryAdapter(dsl, converter))

        val barValid = BarFactory.createValid()

        val genericMessage = "error"
        val genericException = BarException(Throwable(genericMessage))

        every { converter.convert(any()) } returns barValid

        When("uma requested é enviada") {
            every { dsl.selectFrom(BAR).fetch(any<RecordMapper<BarRecord, Bar>>()) } returns listOf(barValid)

            And("aconteceu um erro no service") {
                every { dsl.selectFrom(BAR).fetch(any<RecordMapper<BarRecord, Bar>>()) } throws genericException

                Then("passa para a camada de cima o erro") {
                    shouldThrow<BarException> {
                        repository.all()
                    }.message shouldBe genericMessage
                }
            }

            Then("retornar uma lista com sucesso") {
                val list: List<Bar> = repository.all()
                with(list.first()) {
                    id shouldBe barValid.id
                    name shouldBe barValid.name
                }
            }
        }
    }
})