package com.jtw.scriptutils

import kotlin.test.Test
import kotlin.test.assertEquals

class ExecTests {
    @Test
    fun `parseCommandLine handles quotes`() {
        parseCommandLine(""" echo "hello world" "from tests" """)
            .shouldBe(listOf("echo", "hello world", "from tests"))

        parseCommandLine(""" echo "hello world"."from tests" """)
            .shouldBe(listOf("echo", "hello world.from tests"))

        parseCommandLine(""" echo one "" "two three" """)
            .shouldBe(listOf("echo", "one", "", "two three"))

        parseCommandLine(""" echo one "\"" "two three" """)
            .shouldBe(listOf("echo", "one", "\"", "two three"))

        parseCommandLine(""" echo one"\""two three """)
            .shouldBe(listOf("echo", "one\"two", "three"))
    }
}

infix fun Any?.shouldBe(other: Any) {
    assertEquals(other, this)
}