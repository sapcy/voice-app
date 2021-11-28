package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.data.model.Voice
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("songs/list-artist-top-tracks")
    suspend fun getMusics(): List<Voice>

    @POST("record")
    suspend fun sendRecordFile(): String

    @Multipart
    @POST("test")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>
}