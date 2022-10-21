package com.jtw.scriptutils

import java.io.File

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

fun Iterable<String>.writeTo(file: File, separator: String = "\n") {
    file
        .bufferedWriter()
        .use { bufferedWriter ->
            this.forEach { id ->
                bufferedWriter
                    .append(id)
                    .append(separator)
            }
            bufferedWriter.flush()
        }
}

fun Sequence<String>.writeTo(file: File, separator: String = "\n") {
    // replace fun Sequence<String>.writeLinesToFile(file: File) {
    file
        .bufferedWriter()
        .use { bufferedWriter ->
            this.forEach { id ->
                bufferedWriter
                    .append(id)
                    .append(separator)
            }
            bufferedWriter.flush()
        }
}
