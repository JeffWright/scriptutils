package dev.jtbw.scriptutils

fun String.removeAnsiColors(): String {
  return replace(Regex("""\u001b\[.*?m"""), "")
}
