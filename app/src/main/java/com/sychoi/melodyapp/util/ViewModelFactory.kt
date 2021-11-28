package com.sychoi.melodyapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sychoi.melodyapp.data.api.ApiHelper
import com.sychoi.melodyapp.data.repository.MainRepository
import com.sychoi.melodyapp.ui.main.viewmodel.RecordViewModel
import com.sychoi.melodyapp.ui.main.viewmodel.VoiceViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoiceViewModel::class.java)) {
            return VoiceViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}