package dev.jtbw.scriptutils

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ScriptUtils {
  val retrofit = RetrofitUtils

  val moshi by lazy { Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .addLast(LocalDateTimeAdapter())
    .addLast(LocalDateAdapter())
    .build()
  }
}

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>(){
  @ToJson
  override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
    value?.let { writer.value(it.format(formatter)) }

  }

  @FromJson
  override fun fromJson(reader: JsonReader): LocalDateTime? {
    return if (reader.peek() != JsonReader.Token.NULL) {
      fromNonNullString(reader.nextString())
    } else {
      reader.nextNull<Any>()
      null
    }    }
  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private fun fromNonNullString(nextString: String) : LocalDateTime = LocalDateTime.parse(nextString, formatter)

}

class LocalDateAdapter : JsonAdapter<LocalDate>(){
  @ToJson
  override fun toJson(writer: JsonWriter, value: LocalDate?) {
    value?.let { writer.value(it.format(formatter)) }

  }

  @FromJson
  override fun fromJson(reader: JsonReader): LocalDate? {
    return if (reader.peek() != JsonReader.Token.NULL) {
      fromNonNullString(reader.nextString())
    } else {
      reader.nextNull<Any>()
      null
    }    }
  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
  private fun fromNonNullString(nextString: String) : LocalDate = LocalDate.parse(nextString, formatter)

}
