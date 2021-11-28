package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.data.model.Voice
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Part

interface ApiHelper {
    suspend fun getVoices(): List<Voice>
    suspend fun sendRecordFile(recordFile: String): Boolean
    fun upload(@Part image: MultipartBody.Part,
                       @Part("desc") desc: RequestBody
    ): Call<UploadResponse>
}