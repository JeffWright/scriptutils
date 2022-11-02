package dev.jtbw.scriptutils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type
import retrofit2.Converter
import retrofit2.Retrofit

class StringConverterFactory(private val moshi: Moshi) : Converter.Factory() {
  override fun stringConverter(
      type: Type,
      annotations: Array<out Annotation>,
      retrofit: Retrofit
  ): Converter<*, String>? {
    return when (type) {
      String::class.java -> null
      else -> StringConverter<Any>(moshi.adapter(type, emptySet()))
    }
  }
}

private class StringConverter<T : Any>(private val jsonAdapter: JsonAdapter<T>) :
    Converter<T, String> {
  override fun convert(value: T): String {
    return jsonAdapter.toJson(value)
  }
}
