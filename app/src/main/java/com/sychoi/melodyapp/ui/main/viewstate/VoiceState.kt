package com.sychoi.melodyapp.ui.main.viewstate

import com.sychoi.melodyapp.data.model.Voice

sealed class VoiceState {
    object Idle : VoiceState()
    object Loading : VoiceState()
    data class Voices(val voices: List<Voice>) : VoiceState()
    data class Error(val error: String?) : VoiceState()
}