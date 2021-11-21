package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.Voice

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getVoices(): List<Voice> {
        return apiService.getMusics()
    }

    override suspend fun sendRecordFile(recordFile: String): Boolean {
        TODO("Not yet implemented")
    }
}