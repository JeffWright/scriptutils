package dev.jtbw.scriptutils

infix fun <T : Any> T?.shouldBe(other: T?): T? {
  require(this == other) {
    "should be $other (${other?.javaClass?.simpleName}) but was $this (${this?.javaClass?.simpleName})"
  }
  return this
}

infix fun <T : Any> T?.shouldNotBe(other: T?): T? {
  require(this != other) { "should NOT be $other but was $this" }
  if (this is Number && other is Number) {
    require(this.toDouble() != other.toDouble()) {
      "should NOT be $other (${other.javaClass.simpleName}) but was $this (${this.javaClass.simpleName})"
    }
  }
  return this
}

infix fun <T> Collection<T>.shouldContain(other: T): Collection<T> {
  require(other in this) { "should contain $other but did not: $this" }
  return this
}

infix fun <T> Collection<T>.shouldNotContain(other: T): Collection<T> {
  require(other !in this) {
    "should NOT contain $other but found at index ${indexOf(other)}: $this"
  }
  return this
}
