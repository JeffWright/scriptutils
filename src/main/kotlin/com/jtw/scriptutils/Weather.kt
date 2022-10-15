package com.jtw.scriptutils

internal fun main() {
  "curl wttr.in"()
    .stdout // output lines as Sequence<String>
    .first { it.contains("°F")}
    .removeAnsiColors()
    .match(Regex("""(\d+\(\d+\))"""))
    .println { "Today's Temperature is: $it" }
}
