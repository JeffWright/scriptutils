package com.jtw.scriptutils

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
    val parts = parseCommandLine(command)
    val procBuilder = ProcessBuilder(*parts.toTypedArray())
        .directory(File(PWD))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
    return (procBuilder)
}

internal fun parseCommandLine2(command: String): List<String> {
    val re = Regex("""("(.*?)"\S*)+""")
    val quotedStrings = re.findAll(command).iterator()
    var idx = 0
    val parts = buildList {
        while (quotedStrings.hasNext()) {
            val quotedString = quotedStrings.next()
            // add tokens up until the index of the next match
            addAll(command.substring(idx, quotedString.range.first).split("\\s".toRegex()))
            // add the next match
            add(
                quotedString.groupValues[1]
                    .replace(Regex(""""(.*?)"""")) { m ->
                        m.groupValues[1]
                    }
            )
            idx = quotedString.range.last + 1
        }
        addAll(command.substring(idx).split("\\s".toRegex()))
    }
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    return parts
}

internal fun parseCommandLine(command: String): List<String> {
    val positionsOfNonEnclosedSpace = buildList<Int> {
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
    val tokens = buildList<String> {
        while (spacePositions.hasNext()) {
            val pos = spacePositions.next()
            add(command.substring(cmdIdx, pos))
            cmdIdx = pos + 1
        }
        if (cmdIdx < command.lastIndex) {
            add(command.substring(cmdIdx, command.lastIndex))
        }
    }
    return tokens
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map {
            it.replace("\\\"", "^^ESCAPED QUOTE^^")
                .replace("\"", "")
                .replace("^^ESCAPED QUOTE^^", "\"")
        }

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
    stdout.writeTo(file)
    waitFor()
    return this
}

val ProcessBuilder.stdoutString get() = stdout.joinToString("\n")
val ProcessBuilder.stderrString get() = stderr.joinToString("\n")
