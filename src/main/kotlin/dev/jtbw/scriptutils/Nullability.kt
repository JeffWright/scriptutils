package dev.jtbw.scriptutils

fun <T : Any> T?.requireNotNull(lazyMessage: () -> String): T {
  require(this != null, lazyMessage)
  return this
}
