package dev.jtbw.scriptutils

import dev.jtbw.scriptutils.ListSplitDelimiterOption.*

operator fun <T> Iterable<T>.times(count: Int): List<T> {
  val list = this
  return buildList {
    val builder = this
    repeat(count) { builder.addAll(list) }
  }
}

enum class ListSplitDelimiterOption {
  /** delimiter entries will be omitted */
  OMIT,
  /** delimiter entries will be included at the start of sublists */
  PREPEND,
  /** delimiter entries will be included at the end of sublists */
  APPEND,
}

fun <T> Iterable<T>.split(option: ListSplitDelimiterOption = OMIT, predicate: (T) -> Boolean) : List<List<T>>{
  val src = this
  return buildList {
    val iter = src.iterator()
    var idx= 0
    var listInProgress = mutableListOf<T>()
    iter.forEach {
      if(predicate(it)) {
        when(option) {
          OMIT -> {
            add(listInProgress)
            listInProgress = mutableListOf()
          }
          PREPEND -> {
            add(listInProgress)
            listInProgress = mutableListOf(it)
          }
          APPEND -> {
            listInProgress.add(it)
            add(listInProgress)
            listInProgress = mutableListOf()
          }
        }
      } else {
        listInProgress.add(it)
      }
      idx++
    }
    add(listInProgress)
  }
}

fun <T> listOfInitialized(count: Int, initializer: (Int) -> T) :List<T>{
  return (0..<count).map{initializer(it)}
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
