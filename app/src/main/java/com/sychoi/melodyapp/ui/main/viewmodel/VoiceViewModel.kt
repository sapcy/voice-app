package com.sychoi.melodyapp.ui.main.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.data.model.Voice
import com.sychoi.melodyapp.data.repository.MainRepository
import com.sychoi.melodyapp.ui.main.intent.MainIntent
import com.sychoi.melodyapp.ui.main.viewstate.VoiceState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class VoiceViewModel(private val repository: MainRepository) : ViewModel() {

    val musicIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val state: StateFlow<VoiceState>
        get() = _state

    var voiceList: ArrayList<Voice> = arrayListOf(
        Voice(1, R.drawable.music_icon1, "담아갈게", "코인노래방(최시영)", "3:42", ""),
        Voice(2, R.drawable.music_icon2, "지나오다", "코인노래방(최시영)", "2:52", ""),
        Voice(3, R.drawable.music_icon3, "보고싶다", "보컬레슨(최시영)", "3:36", ""),
        Voice(4, R.drawable.music_icon4, "사랑은 지날수록 선명하게 남아", "코인노래방(최시영)", "3:19", ""),
        Voice(5, R.drawable.music_icon5, "슬픔활용법", "Youtube", "2:52", ""),
        Voice(6, R.drawable.music_icon6, "연인", "코인노래방(최시영)", "4:43", ""),
        Voice(7, R.drawable.music_icon4, "야생화", "코인노래방(최시영)", "3:25", ""),
        Voice(8, R.drawable.music_icon2, "벗", "코인노래방(최시영)", "2:52", ""),
        Voice(9, R.drawable.music_icon1, "제발", "코인노래방(최시영)", "3:27", "")
    )

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            musicIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchVoice -> fetchVoice()
                }
            }
        }
    }

    private fun fetchVoice() {
        viewModelScope.launch {
            _state.value = VoiceState.Loading

            // 임시로 프로그레스바 용도로 추가
            Handler(Looper.getMainLooper()).postDelayed({
                _state.value = try {
//                MainState.Musics(repository.getMusics())
                    VoiceState.Voices(voiceList)
                } catch (e: Exception) {
                    VoiceState.Error(e.localizedMessage)
                }
            }, 2000)

        }
    }

    public fun addMusic(voice: Voice) {
        voiceList.add(voice)
    }
}