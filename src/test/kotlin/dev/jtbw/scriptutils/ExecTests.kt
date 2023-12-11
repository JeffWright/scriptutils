package dev.jtbw.scriptutils

import kotlin.test.Test

class ExecTests {
  @Test
  fun `parseCommandLine handles quotes`() {
    parseCommandLine(""" echo "hello world" "from tests" """)
      .shouldBe(listOf("echo", "hello world", "from tests"))

    parseCommandLine("""echo "hello world" "from tests"""")
      .shouldBe(listOf("echo", "hello world", "from tests"))

    parseCommandLine("""echo "hello world"."from tests"""")
      .shouldBe(listOf("echo", "hello world.from tests"))

    parseCommandLine("""echo one "" "two three"""").shouldBe(listOf("echo", "one", "", "two three"))

    parseCommandLine("""echo one "\"" "two three"""")
      .shouldBe(listOf("echo", "one", "\"", "two three"))

    parseCommandLine("""echo one"\""two three""").shouldBe(listOf("echo", "one\"two", "three"))

    parseCommandLine("""echo one""").shouldBe(listOf("echo", "one"))

    parseCommandLine("""echo one two""").shouldBe(listOf("echo", "one", "two"))
  }

  @Test
  fun `parseCommandLine handles final token of length 1`() {
    val adb = "/Users/user/Library/Android/sdk/platform-tools/adb"
    parseCommandLine("""$adb shell monkey -u 1 -p com.some.package 1""")
      .shouldBe(listOf(adb, "shell", "monkey", "-u", "1", "-p", "com.some.package", "1"))
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

    // TODO JTW can we get it so the - is not required?
    "grep d -"().stdin(input).stdoutString shouldBe "def"

    "grep d -"(input).stdoutString shouldBe "def"

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
