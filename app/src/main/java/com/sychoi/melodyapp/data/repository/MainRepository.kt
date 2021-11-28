package com.sychoi.melodyapp.data.repository

import com.sychoi.melodyapp.data.api.ApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getMusics() = apiHelper.getVoices()
    suspend fun sendRecordFile(recordFile: String) = apiHelper.sendRecordFile(recordFile)
    suspend fun upload(image: MultipartBody.Part, desc: RequestBody) = apiHelper.upload(image, desc)
}