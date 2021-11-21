package com.sychoi.melodyapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

object RetrofitBuilder {
    private const val BASE_URL = "https://shazam.p.rapidapi.com/songs/"
    // https://shazam.p.rapidapi.com/songs/list-artist-top-tracks?id=40008598&locale=en-US/

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(AppInterceptor()))
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("x-rapidapi-host", "shazam.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "853f8321efmsh70746f0350ae165p1194dfjsn9743d1323376")
                .build()

            proceed(newRequest)
        }
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}