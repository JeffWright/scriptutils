package dev.jtbw.scriptutils

fun Sequence<String>.forEachPrint() {
  forEach { println(it) }
}

fun Sequence<String>.onEachPrint(): Sequence<String> {
  return onEach { println(it) }
}

fun <E, T : Iterable<E>> T.forEachPrint(): T {
  forEach { it.println() }
  return this
}

fun String.removeAnsiColors(): String {
  return replace(Regex("""\u001b\[.*?m"""), "")
}
