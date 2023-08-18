package dev.jtbw.scriptutils

import java.io.File

/* ===== Directories ===== */

val `~` = HOME
val HOME
  get() = File(System.getProperty("user.home"))
val PWD
  get() = File(System.getProperty("user.dir"))

fun cd(path: String) {
  System.setProperty("user.dir", path)
}

fun cd(path: File) {
  System.setProperty("user.dir", path.absolutePath)
}

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
