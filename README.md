# scriptutils

Some utilities to make writing scripts in Kotlin easier.

E.g.:
```kotlin
"curl google.com"()
    .dump() // Print stdout and stderr to screen
```
or
```kotlin
"curl wttr.in"()
    .stdout // output lines as Sequence<String>
    .first { it.contains("°F")}
    .removeAnsiColors()
    .match(Regex("""(\d+\(\d+\))""")) // Extension for easier regex matching
    .println { "Today's Temperature is: $it" }
```
