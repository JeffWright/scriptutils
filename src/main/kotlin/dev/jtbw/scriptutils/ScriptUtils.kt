package dev.jtbw.scriptutils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ScriptUtils {
  val retrofit = RetrofitUtils

  val moshi by lazy { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }
}
