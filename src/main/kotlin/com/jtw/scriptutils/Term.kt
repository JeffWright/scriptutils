package com.jtw.scriptutils

import com.github.ajalt.mordant.terminal.Terminal

val term = Terminal()

fun pressEnterToContinue() {
    term.warning("Press Enter to Continue...")
    term.readLineOrNull(false)
}

fun <T> List<T>.choose(prompt: String, display: (T) -> String = { it.toString() }): T {
    term.println(prompt)
    forEachIndexed { idx, item ->
        println("[${idx + 1}] ${display(item)}")
    }

    while (true) {
        term.print("?> ")
        val input = term.readLineOrNull(false)
        val oneBasedIdx = input?.trim()?.toInt() ?: continue
        val zeroBasedIdx = oneBasedIdx - 1
        if (zeroBasedIdx !in 0..this.lastIndex) continue
        return this[zeroBasedIdx]
    }
}

fun <T : Any?> T.println(block: (T) -> Any? = { it }) = term.println(block(this)).let { this }
