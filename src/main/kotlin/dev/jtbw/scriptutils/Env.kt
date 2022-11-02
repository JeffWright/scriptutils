package dev.jtbw.scriptutils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Env {
  val moshi by lazy { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }

  var retrofitLoggingInterceptor =
      HttpLoggingInterceptor { s -> term.info(s) }
          .also { it.level = HttpLoggingInterceptor.Level.NONE }

  fun setRetrofitLogging(level: HttpLoggingInterceptor.Level) {
    retrofitLoggingInterceptor.level = level
  }

  private val client: OkHttpClient by lazy {
    OkHttpClient.Builder().addInterceptor(retrofitLoggingInterceptor).build()
  }

  fun retrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(StringConverterFactory(moshi))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
  }
}
