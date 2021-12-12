package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.data.model.Voice
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("songs/list-artist-top-tracks")
    suspend fun getMusics(): List<Voice>

    @POST("record")
    suspend fun sendRecordFile(): String

    @Multipart
    @POST("voice")
    suspend fun uploadImage(
        @PartMap voiceMap: HashMap<String?, RequestBody?>,
        @PartMap file: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    //@PartMap file: MultipartBody.Part,
    //        @Part("desc") desc: RequestBody
}