package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
//    private const val BASE_URL = "http://192.168.0.7:8080/file/"
    private const val BASE_URL = "http://192.168.0.14:8080/file/"

    private fun getRetrofit(): ApiService {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
    val apiService: ApiService = getRetrofit()

//    private fun provideOkHttpClient(
//        interceptor: AppInterceptor
//    ): OkHttpClient = OkHttpClient.Builder()
//        .run {
//            addInterceptor(interceptor)
//            build()
//        }
//
//    class AppInterceptor : Interceptor {
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain)
//                : Response = with(chain) {
//            val newRequest = request().newBuilder()
//                .addHeader("x-rapidapi-host", "shazam.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "853f8321efmsh70746f0350ae165p1194dfjsn9743d1323376")
//                .build()
//
//            proceed(newRequest)
//        }
//    }
}