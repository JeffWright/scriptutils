package dev.jtbw.scriptutils

import java.io.File
import org.junit.jupiter.api.Test

class FileExtensionsTests {
  @Test
  fun `file path appending`() {
    val dir = File("/home/user")
    (dir / "Downloads" / "Temp").absolutePath shouldBe
      File("/home/user/Downloads/Temp").absolutePath

    ("/home/user" / "Downloads" / "Temp").absolutePath shouldBe
      File("/home/user/Downloads/Temp").absolutePath
  }
}
