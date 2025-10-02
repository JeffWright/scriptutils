package dev.jtbw.scriptutils

import com.squareup.moshi.JsonAdapter
import dev.jtbw.retrofit.RetrofitUtils
import java.io.File

/** Dead-simple file-based json data store */
class Store<T : Any>(
  private val file: File,
  private val adapter: JsonAdapter<T>,
  private val default: () -> T,
) {

  fun overwrite(data: T) {
    file.writeText(adapter.toJson(data))
  }

  fun update(f: (T) -> T) {
    overwrite(f(read()))
  }

  fun read(): T {
    return readOrNull() ?: default()
  }

  fun readOrNull(): T? {
    val json = runCatching { file.readText() }.getOrNull() ?: return null
    return adapter.fromJson(json)
  }
}

inline fun <reified T : Any> Store(file: File, noinline default: () -> T): Store<T> {
  val adapter = RetrofitUtils.moshi.adapter(T::class.java).indent("  ")
  return Store(file, adapter, default)
}
