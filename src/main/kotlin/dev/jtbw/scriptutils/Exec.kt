package dev.jtbw.scriptutils

import java.io.File
import java.lang.RuntimeException
import kotlin.system.exitProcess

data class ProcessDecorator(val command: String, val p: Process)

fun Process.decorate(command: String) = ProcessDecorator(command, this)

fun pipe(vararg commands: String): ProcessDecorator {
  return ProcessDecorator(
    command = commands.joinToString(" | "),
    p =
      ProcessBuilder.startPipeline(
          commands
            .map { cmd -> exec(cmd) }
            .onEach { it.redirectOutput(ProcessBuilder.Redirect.PIPE) }
        )
        .last()
  )
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

operator fun String.invoke(printCommand: Boolean = false): ProcessDecorator {
  if (printCommand) {
    println("$> $this")
  }
  return if (this.contains("|")) {
    pipe(*this.split("|").toTypedArray())
  } else {
    exec(this).start().decorate(this)
  }
}

// TODO JTW
// operator fun String.invoke(process: ProcessDecorator) = this().stdin(process)
operator fun String.invoke(byteArray: ByteArray) = this().stdin(byteArray)

operator fun String.invoke(file: File) = this().stdin(file)

operator fun String.invoke(string: String) = this().stdin(string)

/* ===== STDIN ===== */

val ProcessDecorator.stdin
  get() = p.outputStream

fun ProcessDecorator.stdin(byteArray: ByteArray): ProcessDecorator {
  stdin.write(byteArray)
  stdin.close()
  return this
}

fun ProcessDecorator.stdin(file: File) = stdin(file.readLines().joinToString("\n").toByteArray())

fun ProcessDecorator.stdin(string: String): ProcessDecorator = stdin(string.toByteArray())

// TODO JTW implement:
// fun Process.stdin(process: Process): Process {
// }

/* ===== STDOUT ===== */

val ProcessDecorator.stdout: Sequence<String>
  get() = p.inputStream.bufferedReader().lineSequence()
val ProcessDecorator.stdoutString: String
  get() = stdout.joinToString("\n")

fun ProcessDecorator.stdout(file: File): ProcessDecorator {
  stdout.writeTo(file)
  p.waitFor()
  return this
}

/* ===== STDERR ===== */

val ProcessDecorator.stderr: Sequence<String>
  get() = p.errorStream.bufferedReader().lineSequence()
val ProcessDecorator.stderrString: String
  get() = stderr.joinToString("\n")

fun ProcessDecorator.stderr(file: File): ProcessDecorator {
  stderr.writeTo(file)
  p.waitFor()
  return this
}

/** Print process' stdout and stderr to our stdout and stderr */
fun ProcessDecorator.printLns(): ProcessDecorator {
  stdout.printlns()
  stderr.forEach { System.err.println(it) }
  p.waitFor()
  return this
}

fun ProcessDecorator.waitFor(): ProcessDecorator {
  p.waitFor()
  return this
}

fun ProcessDecorator.exitValue(): Int = p.exitValue()

/** like waitFor(), but exits if the given process has a non-zero exit code */
fun ProcessDecorator.waitForOk(
  printStackTrace: Boolean = false,
  lazyMessage: () -> Any = { "" }
): ProcessDecorator {
  when (val exitCode = p.waitFor()) {
    0 -> return this
    else -> {
      term.forStdErr().danger("Command failed with exit code $exitCode: \n$> $command")
      this.println()
      term.forStdErr().danger(lazyMessage())
      if (printStackTrace) {
        RuntimeException().printStackTrace()
      }
      exitProcess(exitCode)
    }
  }
}

fun ProcessDecorator.success(): Boolean = p.waitFor() == 0
