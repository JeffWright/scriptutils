package dev.jtbw.scriptutils

internal fun main() {
  processDemo()
  terminalDemo()
}

internal fun processDemo() {
  // use String.invoke() to execute shell commands
  "ps aux"()
    .stdout // Sequence<String>
    .filter { it.contains("intellij", ignoreCase = true) }
    .print()

  // use .div on File or String to build paths
  val downloadsDir = `~` / "Downloads"
  val somefile = downloadsDir / "somefile.txt"

  // Pipe commands to each other automatically
  "ls $downloadsDir | grep a -"().stdout(somefile) // Write output to file

  // Several ways to pass stdin to commands:
  "grep patch -"(somefile).print() // Print stdout and stderr to screen
  // ...or...
  "grep patch -"().stdin(somefile).print()
  // ...or...
  "grep patch -"()
    .apply {
      // OutputStream
      stdin.writeLine("patch 1")
      stdin.writeLine("patch 2")
      stdin.writeLine("other")
      stdin.close()
    }
    .print()

  "curl wttr.in"()
    .stdout // Sequence<String>
    .first { it.contains("°F") }
    .removeAnsiColors()
    .match(Regex("""(\d+\(\d+\))""")) // Extension for easier regex matching
    .print { "Today's Temperature is: $it" }
}

internal fun terminalDemo() {
  println("doing some work...")
  pressEnterToContinue()

  val month: String = listOf("Jan", "Feb", "Mar").choose("Pick a month")
  month.print { "You chose: $it" }

  val text: String = prompt("Please enter some text")
  text.print { "You entered: $it" }
}
