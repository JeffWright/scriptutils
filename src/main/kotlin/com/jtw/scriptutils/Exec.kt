package com.jtw.scriptutils

import com.github.ajalt.mordant.terminal.Terminal
import java.io.File

fun cd(path: String) {
  System.setProperty("user.dir", path)
}

fun pipe(vararg processBuilders: ProcessBuilder): Process {
  return ProcessBuilder.startPipeline(
    processBuilders.toList()
      .onEach { it.redirectOutput(ProcessBuilder.Redirect.PIPE) }
  )
    .last()
}

fun exec(command: String): ProcessBuilder {
  val parts = command.split("\\s".toRegex())
  val procBuilder = ProcessBuilder(*parts.toTypedArray())
    .directory(File(PWD))
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE)
  return (procBuilder)
}

operator fun String.invoke() = exec(this)

val `~` = HOME
val HOME get() = System.getProperty("user.home")
val PWD get() = System.getProperty("user.dir")

val ProcessBuilder.stdout get() = start().stdout
val ProcessBuilder.stderr get() = start().stderr

val Process.stdout get() = inputStream.bufferedReader().lineSequence()
val Process.stderr get() = errorStream.bufferedReader().lineSequence()
val Process.stdin get() = outputStream

fun ProcessBuilder.dump() {
  start().dump()
}

fun ProcessBuilder.waitFor() {
  start().waitFor()
}

fun Process.dump() {
  stdout.map { "stdout> $it" }.forEachPrint()
  stderr.map { "stderr> $it" }.forEachPrint()
  waitFor()
}

fun Process.stdin(byteArray: ByteArray): Process {
  stdin.write(byteArray)
  stdin.close()
  return this
}

fun Process.stdin(file: File) = stdin(
  file
    .readLines()
    .joinToString("\n")
    .toByteArray()
)

fun Process.stdin(string: String): Process = stdin(string.toByteArray())

fun Process.stdout(file: File): Process {
  stdout.writeLinesToFile(file)
  waitFor()
  return this
}
