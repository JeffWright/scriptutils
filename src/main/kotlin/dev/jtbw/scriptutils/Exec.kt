package dev.jtbw.scriptutils

import java.io.File
import java.lang.RuntimeException
import kotlin.system.exitProcess

fun cd(path: String) {
  System.setProperty("user.dir", path)
}

fun cd(path: File) {
    System.setProperty("user.dir", path.absolutePath)
}

fun pipe(vararg commands: String): Process {
  return ProcessBuilder.startPipeline(
          commands
              .map { cmd -> exec(cmd) }
              .onEach { it.redirectOutput(ProcessBuilder.Redirect.PIPE) })
      .last()
}

fun exec(command: String): ProcessBuilder {
  val parts = parseCommandLine(command)
  val procBuilder =
      ProcessBuilder(*parts.toTypedArray())
          .directory(PWD)
          .redirectOutput(ProcessBuilder.Redirect.PIPE)
          .redirectError(ProcessBuilder.Redirect.PIPE)
  return (procBuilder)
}

internal fun parseCommandLine(command: String): List<String> {
  val command = command.trim()
  val positionsOfNonEnclosedSpace =
      buildList<Int> {
        var enclosed = false
        var escaped = false
        command.forEachIndexed { idx, char ->
          if (!enclosed && (char == ' ' || char == "\t".first())) {
            add(idx)
          } else if (!escaped && char == '"') {
            enclosed = !enclosed
          }

          escaped = (char == '\\')
        }
      }

  val spacePositions = positionsOfNonEnclosedSpace.iterator()
  var cmdIdx = 0
  val tokens =
      buildList<String> {
        while (spacePositions.hasNext()) {
          val pos = spacePositions.next()
          add(command.substring(cmdIdx, pos))
          cmdIdx = pos + 1
        }
        if (cmdIdx <= command.lastIndex) {
          add(command.substring(cmdIdx, command.length))
        }
      }
  return tokens
      .map { it.trim() }
      .filter { it.isNotEmpty() }
      .map {
        it.replace("\\\"", "^^ESCAPED QUOTE^^").replace("\"", "").replace("^^ESCAPED QUOTE^^", "\"")
      }
}

operator fun String.invoke(printCommand: Boolean = false): Process {
  if(printCommand) {
    println("$> $this")
  }
  return if (this.contains("|")) {
    pipe(*this.split("|").toTypedArray())
  } else {
    exec(this).start()
  }
}

// TODO JTW
// operator fun String.invoke(process: Process) = this().stdin(process)
operator fun String.invoke(byteArray: ByteArray) = this().stdin(byteArray)

operator fun String.invoke(file: File) = this().stdin(file)

operator fun String.invoke(string: String) = this().stdin(string)

/* ===== Directories ===== */

val `~` = HOME
val HOME
  get() = File(System.getProperty("user.home"))
val PWD
  get() = File(System.getProperty("user.dir"))

/* ===== STDIN ===== */

val Process.stdin
  get() = outputStream

fun Process.stdin(byteArray: ByteArray): Process {
  stdin.write(byteArray)
  stdin.close()
  return this
}

fun Process.stdin(file: File) = stdin(file.readLines().joinToString("\n").toByteArray())

fun Process.stdin(string: String): Process = stdin(string.toByteArray())

// TODO JTW implement:
// fun Process.stdin(process: Process): Process {
// }

/* ===== STDOUT ===== */

val Process.stdout
  get() = inputStream.bufferedReader().lineSequence()
val Process.stdoutString
  get() = stdout.joinToString("\n")

fun Process.stdout(file: File): Process {
  stdout.writeTo(file)
  waitFor()
  return this
}

/* ===== STDERR ===== */

val Process.stderr
  get() = errorStream.bufferedReader().lineSequence()
val Process.stderrString
  get() = stderr.joinToString("\n")

fun Process.stderr(file: File): Process {
  stderr.writeTo(file)
  waitFor()
  return this
}

/** Print process' stdout and stderr to our stdout and stderr */
fun Process.print(): Process {
  stdout.print()
  stderr.forEach { System.err.println(it) }
  waitFor()
  return this
}

/** like waitFor(), but exits if the given process has a non-zero exit code */
fun Process.waitForOk(printStackTrace: Boolean = false): Process {
  val cmdLine : String? = info().commandLine()
    .let { if(it.isPresent) it.get() else null }

  when(val exitCode = waitFor()) {
    0 -> return this
    else -> {
      term.forStdErr().danger("Command failed with exit code $exitCode: $cmdLine")
      this.print()
      if(printStackTrace) {
        RuntimeException().printStackTrace()
      }
      exitProcess(exitCode)
    }
  }
}

fun Process.exitCodeOk(): Boolean = waitFor() == 0
