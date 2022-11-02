package dev.jtbw.scriptutils

import java.io.File

/**
 * ```
 * val parentDir = File("/Users/blah/Downloads")
 * (parentDir / "something.txt").exists()
 * ```
 */
operator fun File.div(child: String) = File(absolutePath, child)

operator fun String.div(child: String) = File(this, child)

fun Iterable<String>.writeTo(file: File, separator: String = "\n") {
  file.bufferedWriter().use { bufferedWriter ->
    this.forEach { id -> bufferedWriter.append(id).append(separator) }
    bufferedWriter.flush()
  }
}

fun Sequence<String>.writeTo(file: File, separator: String = "\n") {
  // replace fun Sequence<String>.writeLinesToFile(file: File) {
  file.bufferedWriter().use { bufferedWriter ->
    this.forEach { id -> bufferedWriter.append(id).append(separator) }
    bufferedWriter.flush()
  }
}
