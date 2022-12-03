package dev.jtbw.scriptutils

import com.github.ajalt.mordant.terminal.Terminal

val term = Terminal()

fun pressEnterToContinue() {
  term.warning("Press Enter to Continue...")
  term.readLineOrNull(false)
}

fun <T> List<T>.choose(prompt: String, display: (T) -> String = { it.toString() }): T {
  term.println(prompt)
  forEachIndexed { idx, item -> println("[${idx + 1}] ${display(item)}") }

  while (true) {
    term.print("?> ")
    val input = term.readLineOrNull(false)
    val oneBasedIdx = input?.trim()?.toInt() ?: continue
    val zeroBasedIdx = oneBasedIdx - 1
    if (zeroBasedIdx !in 0..this.lastIndex) continue
    return this[zeroBasedIdx]
  }
}

fun prompt(message: String): String {
  println(message)
  print("?> ")
  return readln().trim()
}

fun <T> choose(prompt: String, vararg choices: T, display: (T) -> String = { it.toString() }): T {
  // TODO JTW: also add fancy stuff like "Yes/No" -> can type y, etc
  return choices.toList().choose(prompt, display)
}

fun <T : Any?> T.print(block: (T) -> Any? = { it }) = term.println(block(this)).let { this }

fun Sequence<String>.print() {
  forEach { println(it) }
}

fun Sequence<String>.onEachPrint(): Sequence<String> {
  return onEach { println(it) }
}

fun <E, T : Iterable<E>> T.print(): T {
  forEach { it.print() }
  return this
}
