package com.sychoi.melodyapp.ui.main.viewstate

sealed class RecordState {
    object Idle : RecordState()
    object Loading : RecordState()
    object StartRecording : RecordState()
    object PauseRecording : RecordState()
    object ResumeRecording : RecordState()
    object StopRecording : RecordState()
    object SendRecordFile : RecordState()
    data class Record(val recordCheck: Boolean) : RecordState()
    data class Error(val error: String?) : RecordState()
}