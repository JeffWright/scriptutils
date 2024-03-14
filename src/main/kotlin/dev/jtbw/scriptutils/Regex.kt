package dev.jtbw.scriptutils

/** Convenience function for the common case of "match regex, and find the contents of group 1" */
fun String.match(regex: Regex, group: Int = 1): String {
  return matchOrNull(regex, group) ?: error("No match found! for Regex($regex)  -- input: $this")
}

fun String.matchOrNull(regex: Regex, group: Int = 1): String? {
  return regex.find(this)?.groupValues?.get(group)
}

fun String.matchGroups(regex: Regex): List<String> {
  return (regex.find(this) ?: error("No match found! for Regex($regex)  -- input: $this"))
    .groupValues
    .drop(1)
}

fun String.matchGroupsOrNull(regex: Regex): List<String>? {
  return (regex.find(this) ?: return null)
    .groupValues
    .drop(1)
}
