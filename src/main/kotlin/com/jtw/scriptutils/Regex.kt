package com.jtw.scriptutils

fun String.match(regex: Regex, group: Int = 1) : String {
  return regex.find(this)
    ?.groupValues
    ?.get(group)
    ?: error("No match found! for Regex($regex)  -- input: $this")
}

fun String.matchOrNull(regex: Regex, group: Int = 1) : String? {
  return regex.find(this)
    ?.groupValues
    ?.get(group)
}
