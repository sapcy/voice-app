package com.sychoi.melodyapp.data.api

import com.sychoi.melodyapp.data.model.Voice

interface ApiHelper {
    suspend fun getVoices(): List<Voice>
    suspend fun sendRecordFile(recordFile: String): Boolean
}