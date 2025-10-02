package dev.jtbw.scriptutils

import kotlin.test.Test

class ExecTests {
  @Test
  fun `parseCommandLine handles quotes`() {
    (""" echo "hello world" "from tests" """)
      .invoke()
      .stdoutString
      .shouldBe("hello world from tests")
  }

  @Test
  fun `wildcards`() {
    val out = "ls ./*"().stdoutString
    println(out)
    out.contains("src") shouldBe true
    out.contains("build.gradle.kts") shouldBe true
  }

  @Test
  fun `stdin stdout stderr`() {
    "ls ."().stdout.contains("src") shouldBe true

    val input =
      """
            abc
            def
            ghi
        """
        .trimIndent()

    "cat"().stdin(input).stdoutString shouldBe input

    "cat"(input).stdin(input).stdoutString shouldBe input

    "grep d"().stdin(input).stdoutString shouldBe "def"

    "grep d"(input).stdoutString shouldBe "def"

    pipe("ls .", "grep r -").stdout.toList().apply {
      contains("build.gradle.kts") shouldBe true
      contains("gradlew.bat") shouldBe true
      contains("src") shouldBe true
      contains("README.md") shouldBe false
    }

    "ls . | grep r -"().stdout.toList().apply {
      contains("build.gradle.kts") shouldBe true
      contains("gradlew.bat") shouldBe true
      contains("src") shouldBe true
      contains("README.md") shouldBe false
    }

    // TODO JTW not yet implemented
    /*
    "grep r -"("ls ."())
        .stdout
        .toList()
        .apply {
            contains("build.gradle.kts") shouldBe true
            contains("gradlew.bat") shouldBe true
            contains("src") shouldBe true
            contains("README.md") shouldBe false
        }
     */
  }
}
