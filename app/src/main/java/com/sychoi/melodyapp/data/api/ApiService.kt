package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.Voice
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("songs/list-artist-top-tracks")
    suspend fun getMusics(): List<Voice>

    @POST("record")
    suspend fun sendRecordFile(): String
}