package dev.jtbw.scriptutils

import org.junit.jupiter.api.Test

class RegexTests {
    @Test
    fun match() {
        "the number is 42.".apply {
                match(Regex("(\\d+)"))
                .shouldBe("42")

            matchOrNull(Regex("no(\\d+)"))
                .shouldBe(null)

            val (a, b, c) = matchGroups(Regex("(\\S+) (\\S+) (\\S+) "))
            a.shouldBe("the")
            b.shouldBe("number")
            c.shouldBe("is")
            }
    }
}