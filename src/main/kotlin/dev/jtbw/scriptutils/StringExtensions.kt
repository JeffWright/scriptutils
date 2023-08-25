package dev.jtbw.scriptutils

fun String.removeAnsiColors(): String {
  return replace(Regex("""\u001b\[.*?m"""), "")
}

operator fun String.times(i: Int) = repeat(i)
