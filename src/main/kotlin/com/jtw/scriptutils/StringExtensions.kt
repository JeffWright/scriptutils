package com.jtw.scriptutils

import java.io.File

fun Sequence<String>.forEachPrint() {
    forEach { println(it) }
}

fun Sequence<String>.onEachPrint(): Sequence<String> {
    return onEach { println(it) }
}

fun Sequence<String>.writeLinesToFile(file: File) {
    val writer = file.bufferedWriter()
    this.forEach { line ->
        writer.write(line)
        writer.newLine()
    }
    writer.flush()
}

fun String.removeAnsiColors(): String {
    return replace(Regex("""\u001b\[.*?m"""), "")
}