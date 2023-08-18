package dev.jtbw.scriptutils

import java.io.OutputStream

fun OutputStream.writeLine(line: String) {
  write(line.toByteArray())
  write("\n".toByteArray())
}
