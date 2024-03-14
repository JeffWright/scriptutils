package dev.jtbw.scriptutils

import org.junit.jupiter.api.Test

class ListExtensionTests {
  @Test
  fun testMultiply() {
    (listOf(1, 2, 3) * 3) shouldBe listOf(1, 2, 3, 1, 2, 3, 1, 2, 3)
  }

  @Test
  fun testSplit() {
    // Just here to note that the behavior is analogous
    "12345".split('3', '4') shouldBe listOf("12", "", "5")
    "12345".split('5') shouldBe listOf("1234", "")

    listOf(1, 2, 3, 4, 5).split { it == 3 || it == 4 } shouldBe
      listOf(listOf(1, 2), emptyList(), listOf(5))
    listOf(1, 2, 3, 4, 5).split { it == 5 } shouldBe listOf(listOf(1, 2, 3, 4), emptyList())

    listOf(1, 2, 3, 4, 5).split(ListSplitDelimiterOption.PREPEND) { it == 3 || it == 4 } shouldBe
      listOf(listOf(1, 2), listOf(3), listOf(4, 5))
    listOf(1, 2, 3, 4, 5).split(ListSplitDelimiterOption.APPEND) { it == 5 } shouldBe
      listOf(listOf(1, 2, 3, 4, 5), emptyList())
  }

  @Test
  fun testInitializedList() {
    listOfInitialized(3) { null } shouldBe listOf(null, null, null)
    listOfInitialized(3) { it + 2 } shouldBe listOf(2, 3, 4)
  }
}
