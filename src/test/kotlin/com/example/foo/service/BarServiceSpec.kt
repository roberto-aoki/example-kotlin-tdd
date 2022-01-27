package com.example.foo.service

import com.example.foo.exception.BarException
import com.example.foo.factory.BarFactory
import com.example.foo.model.Bar
import com.example.foo.repository.BarRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk

class BarServiceSpec : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    Given("um service que manipula o bar") {
        val repository: BarRepository = mockk()
        val service: BarService = spyk(BarServiceAdapter(repository))

        val barValid = BarFactory.createValid()

        val genericMessage = "error"
        val genericException = BarException(Throwable(genericMessage))

        When("uma requested é enviada") {
            every { repository.all() } returns listOf(barValid)

            And("aconteceu um erro no service") {
                every { repository.all() } throws genericException

                Then("passa para a camada de cima o erro") {
                    shouldThrow<BarException> {
                        service.getList()
                    }.message shouldBe genericMessage
                }
            }

            Then("retornar uma lista com sucesso") {
                val list: List<Bar> = service.getList()
                with(list.first()) {
                    id shouldBe barValid.id
                    name shouldBe barValid.name
                }
            }
        }
    }
})
