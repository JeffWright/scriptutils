package dev.jtbw.scriptutils

import com.squareup.moshi.JsonAdapter
import dev.jtbw.scriptutils.ScriptUtils.moshi
import java.io.File

/** Dead-simple file-based json data store */
class Store<T : Any>(
  private val file: File,
  private val adapter: JsonAdapter<T>,
  private val default: () -> T
) {

  fun overwrite(data: T) {
    file.writeText(adapter.toJson(data))
  }

  fun update(f: (T) -> T) {
    overwrite(f(read()))
  }

  fun read(): T {
    val json = runCatching { file.readText() }.getOrNull() ?: return default()
    return adapter.fromJson(json) ?: default()
  }
}

inline fun <reified T : Any> Store(file: File, noinline default: () -> T): Store<T> {
  val adapter = moshi.adapter(T::class.java).indent("  ")
  return Store(file, adapter, default)
}
