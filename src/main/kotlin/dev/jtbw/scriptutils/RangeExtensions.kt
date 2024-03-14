package dev.jtbw.scriptutils

fun LongRange.shift(delta: Long): LongRange = LongRange(start + delta, endInclusive + delta)

val LongRange.size
  get() = endInclusive - start + 1

fun IntRange.shift(delta: Int): IntRange = IntRange(start + delta, endInclusive + delta)

val IntRange.size
  get() = endInclusive - start + 1
