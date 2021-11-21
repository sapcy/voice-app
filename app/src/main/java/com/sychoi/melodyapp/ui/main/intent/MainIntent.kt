package com.sychoi.melodyapp.ui.main.intent

sealed class MainIntent {
    object FetchVoice : MainIntent()
    object StartRecord : MainIntent()
    object ResumeRecord : MainIntent()
    object StopRecord : MainIntent()
    object PauseRecord : MainIntent()
    object SendRecordFile : MainIntent()
}