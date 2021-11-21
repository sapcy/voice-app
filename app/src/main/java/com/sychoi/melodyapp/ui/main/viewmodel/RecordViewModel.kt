package com.sychoi.melodyapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sychoi.melodyapp.data.repository.MainRepository
import com.sychoi.melodyapp.ui.main.intent.MainIntent
import com.sychoi.melodyapp.ui.main.viewstate.RecordState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RecordViewModel(private val repository: MainRepository) : ViewModel() {

    val recordIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<RecordState>(RecordState.Idle)
    val state: StateFlow<RecordState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            recordIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.StartRecord -> startRecord()
                    is MainIntent.PauseRecord -> pauseRecord()
                    is MainIntent.ResumeRecord -> resumeRecord()
                    is MainIntent.StopRecord -> stopRecord()
//                    is MainIntent.SendRecordFile -> sendRecord()
                }
            }
        }
    }

    private fun startRecord() {
        viewModelScope.launch {
            _state.value = RecordState.StartRecording
        }
    }

    private fun pauseRecord() {
        viewModelScope.launch {
            _state.value = RecordState.PauseRecording
        }
    }

    private fun stopRecord() {
        viewModelScope.launch {
            _state.value = RecordState.StopRecording
        }
    }

    private fun resumeRecord() {
        viewModelScope.launch {
            _state.value = RecordState.ResumeRecording
        }
    }

    private fun sendRecord() {
        viewModelScope.launch {
            _state.value = RecordState.SendRecordFile

            _state.value = try {
                RecordState.Record(repository.sendRecordFile(""))
            } catch (e: Exception) {
                RecordState.Error(e.localizedMessage)
            }
        }
    }
}