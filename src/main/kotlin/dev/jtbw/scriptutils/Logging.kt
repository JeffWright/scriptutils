package dev.jtbw.scriptutils

fun main() {
  listOf("Dog", "Cat", "Moose").printlns()

  "Landing".println { "Moon $it" }
}

fun <T : Any?> T.println(block: (T) -> Any? = { it }) = term.println(block(this)).let { this }

fun Sequence<String>.printlns() {
  forEach { println(it) }
}

fun Sequence<String>.onEachPrint(): Sequence<String> {
  return onEach { println(it) }
}

fun <E, T : Iterable<E>> T.printlns(): T {
  forEach { it.println() }
  return this
}
