package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.data.model.Voice
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Part

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getVoices(): List<Voice> {
        return apiService.getMusics()
    }

    override suspend fun sendRecordFile(recordFile: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun upload(image: MultipartBody.Part, desc: RequestBody): Call<UploadResponse> {
        return apiService.uploadImage(image, desc)
    }
}