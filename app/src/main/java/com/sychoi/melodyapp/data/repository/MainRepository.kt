package com.sychoi.melodyapp.data.repository

import com.sychoi.melodyapp.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getMusics() = apiHelper.getVoices()
    suspend fun sendRecordFile(recordFile: String) = apiHelper.sendRecordFile(recordFile)
}