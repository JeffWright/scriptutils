package dev.jtbw.scriptutils

import java.time.Duration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitUtils {
  var retrofitLoggingInterceptor =
    HttpLoggingInterceptor { s -> term.info(s) }
      .also { it.level = HttpLoggingInterceptor.Level.NONE }

  fun setRetrofitLogging(level: HttpLoggingInterceptor.Level) {
    retrofitLoggingInterceptor.level = level
  }

  //private val clientBuilder: OkHttpClient.Builder get() =
  //  OkHttpClient.Builder()
  //    .addInterceptor(retrofitLoggingInterceptor)
  //    .callTimeout(Duration.ofDays(1))

  fun retrofit(baseUrl: String, clientBuilder: (OkHttpClient.Builder) -> OkHttpClient.Builder = {it}, builderBlock: Retrofit.Builder.() -> Unit = {}): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(
        clientBuilder(OkHttpClient.Builder())
          .addInterceptor(retrofitLoggingInterceptor)
          .callTimeout(Duration.ofDays(1))
          .build())
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create(ScriptUtils.moshi))
      .apply(builderBlock)
      .build()
  }
}
