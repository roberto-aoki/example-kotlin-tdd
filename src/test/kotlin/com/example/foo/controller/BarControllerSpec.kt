package com.example.foo.controller

import com.example.foo.exception.BarException
import com.example.foo.factory.BarFactory
import com.example.foo.model.Bar
import com.example.foo.service.BarService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify

class BarControllerSpec : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest

    Given("uma api que manipula o bar") {
        val service: BarService = mockk()
        val controller: BarController = spyk(BarController(service))

        val barValid = BarFactory.createValid()

        val genericMessage = "error"
        val genericException = BarException(Throwable(genericMessage))

        When("uma requested é enviada") {
            every { service.getList() } returns listOf(barValid)

            And("aconteceu um erro no service") {
                every { service.getList() } throws genericException

                Then("passa para a camada de cima o erro") {
                    shouldThrow<BarException> {
                        controller.getList()
                    }.message shouldBe genericMessage
                }
            }

            Then("retornar uma lista com sucesso") {
                val list: List<Bar> = controller.getList()
                with(list.first()) {
                    id shouldBe barValid.id
                    name shouldBe barValid.name
                }

                verify(exactly = 1) { service.getList() }
            }
        }
    }
})