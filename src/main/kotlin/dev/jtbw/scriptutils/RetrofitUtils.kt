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

  private val client: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .addInterceptor(retrofitLoggingInterceptor)
      .callTimeout(Duration.ofDays(1))
      .build()
  }

  fun retrofit(baseUrl: String, builderBlock: Retrofit.Builder.() -> Unit = {}): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create(ScriptUtils.moshi))
      .apply(builderBlock)
      .build()
  }
}
