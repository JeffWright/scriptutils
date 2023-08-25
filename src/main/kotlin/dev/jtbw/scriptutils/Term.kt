package dev.jtbw.scriptutils

import com.github.ajalt.mordant.terminal.Terminal
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.system.exitProcess

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
    val oneBasedIdx = input?.trim()?.toIntOrNull() ?: continue
    val zeroBasedIdx = oneBasedIdx - 1
    if (zeroBasedIdx !in 0..this.lastIndex) continue
    return this[zeroBasedIdx]
  }
}

fun yesNo(prompt: String, default: Boolean = false): Boolean {
  while (true) {
    term.print(prompt)
    if (default) {
      term.print(" [Y/n] ")
    } else {
      term.print(" [y/N] ")
    }
    val input = term.readLineOrNull(hideInput = false) ?: ""
    if (input.isBlank()) {
      return default
    }
    when (input.first().lowercase()) {
      "y" -> return true
      "n" -> return false
    }
  }
}

fun prompt(message: String): String {
  println(message)
  print("?> ")
  return readln().trim()
}

fun <T> choose(prompt: String, vararg choices: T, display: (T) -> String = { it.toString() }): T {
  return choices.toList().choose(prompt, display)
}

/** Like require, but prints & exits instead of throwing */
@OptIn(ExperimentalContracts::class)
fun requireOrExit(value: Boolean, lazyMessage: () -> Any?) {
  contract { returns() implies value }
  if (!value) {
    val message = lazyMessage()
    term.danger(lazyMessage())
    exitProcess(1)
  }
}

internal fun main() {
  if (yesNo("Feeling good?", default = true)) {
    println("Good!")
  } else {
    println("Bad!")
  }
}
