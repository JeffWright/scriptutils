package dev.jtbw.scriptutils

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ScriptUtils {
  val retrofit = RetrofitUtils

  val moshi by lazy {
    Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .addLast(LocalDateTimeAdapter()) // TODO JTW add dependency on retrofit util lib
      .addLast(LocalDateAdapter())
      .build()
  }
}

