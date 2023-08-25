package dev.jtbw.scriptutils

operator fun <T> List<T>.times(count: Int): List<T> {
  val list = this
  return buildList {
    val builder = this
    repeat(count) { builder.addAll(list) }
  }
}

fun main() {
  println(listOf(1, 2, 3) * 3)
}

operator fun <T> List<T>.component6() = this[5]

operator fun <T> List<T>.component7() = this[6]

operator fun <T> List<T>.component8() = this[7]

operator fun <T> List<T>.component9() = this[8]

operator fun <T> List<T>.component10() = this[9]

operator fun <T> List<T>.component11() = this[10]

operator fun <T> List<T>.component12() = this[11]

operator fun <T> List<T>.component13() = this[12]

operator fun <T> List<T>.component14() = this[13]

operator fun <T> List<T>.component15() = this[14]

operator fun <T> List<T>.component16() = this[15]

operator fun <T> List<T>.component17() = this[16]

operator fun <T> List<T>.component18() = this[17]

operator fun <T> List<T>.component19() = this[18]

operator fun <T> List<T>.component20() = this[19]
