package com.sychoi.melodyapp.ui.main.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.data.api.ApiHelperImpl
import com.sychoi.melodyapp.data.api.RetrofitBuilder
import com.sychoi.melodyapp.databinding.FragmentRecordBinding
import com.sychoi.melodyapp.ui.main.intent.MainIntent
import com.sychoi.melodyapp.ui.main.view.MainActivity
import com.sychoi.melodyapp.ui.main.viewmodel.RecordViewModel
import com.sychoi.melodyapp.ui.main.viewstate.RecordState
import com.sychoi.melodyapp.util.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class RecordFragment : Fragment(R.layout.fragment_record) {
    private lateinit var mainActivity: MainActivity
    private lateinit var recordViewModel: RecordViewModel
    private lateinit var binding: FragmentRecordBinding
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecordBinding.bind(view)

        setupViewModel()
        observeViewModel(binding)

        mediaRecorder = MediaRecorder()

//        val context = this.context
//        var audioDir: String = ""
//        if (context != null) {
//            audioDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AudioMemos").absolutePath
//        }

//        output = audioDir + "/sample_record.mp3"
        output = "${requireActivity().externalCacheDir?.absolutePath}" + "/sample_record.mp3"
        Log.d("OUTPUT PATH", output!!)
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)

        binding.btnStartRecording.setOnClickListener {
            if (ContextCompat.checkSelfPermission(mainActivity,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(mainActivity, permissions,0)
            } else {
                startRecording()
                binding.tvTime.base = SystemClock.elapsedRealtime()
                binding.tvTime.start()
                binding.recordLottieAnimationView.playAnimation()
            }
        }

        binding.btnStopRecording.setOnClickListener {
            stopRecording()
            binding.tvTime.base = SystemClock.elapsedRealtime()
            binding.recordLottieAnimationView.pauseAnimation()
        }

        binding.btnPauseRecording.setOnClickListener {
            pauseRecording()
            binding.tvTime.stop()
            binding.recordLottieAnimationView.pauseAnimation()
        }

        binding.btnResumeRecording.setOnClickListener {
            resumeRecording()
            binding.tvTime.start()
            binding.recordLottieAnimationView.playAnimation()
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            lifecycleScope.launch {
                recordViewModel.recordIntent.send(MainIntent.StartRecord)
                Log.d("StartRecording", "STARTED RECORDING INTENT SENT!!")
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording(){
        if (state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
            lifecycleScope.launch {
                recordViewModel.recordIntent.send(MainIntent.StopRecord)
            }
        } else{
            Toast.makeText(mainActivity, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        if (state) {
            if (!recordingStopped){
                mediaRecorder?.pause()
                recordingStopped = true
                lifecycleScope.launch {
                    recordViewModel.recordIntent.send(MainIntent.PauseRecord)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        mediaRecorder?.resume()
        lifecycleScope.launch {
            recordViewModel.recordIntent.send(MainIntent.ResumeRecord)
        }
        recordingStopped = false
    }

    private fun setupViewModel() {
        recordViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(
                    RetrofitBuilder.apiService
                )
            )
        ).get(RecordViewModel::class.java)
    }

    private fun observeViewModel(binding: FragmentRecordBinding) {
        lifecycleScope.launch {
            recordViewModel.state.collect {
                when (it) {
                    is RecordState.Idle -> {
                        binding.btnPauseRecording.visibility = View.GONE
                        binding.btnStopRecording.visibility = View.GONE
                        binding.btnStartRecording.visibility = View.VISIBLE
                    }
                    is RecordState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is RecordState.StartRecording -> {
                        binding.btnResumeRecording.visibility = View.GONE
                        binding.btnPauseRecording.visibility = View.VISIBLE
                        binding.btnStopRecording.visibility = View.VISIBLE
                        binding.btnStartRecording.visibility = View.GONE
                    }
                    is RecordState.PauseRecording -> {
                        binding.btnResumeRecording.visibility = View.VISIBLE
                        binding.btnPauseRecording.visibility = View.GONE
                    }
                    is RecordState.ResumeRecording -> {
                        binding.btnPauseRecording.visibility = View.VISIBLE
                        binding.btnResumeRecording.visibility = View.GONE
                    }
                    is RecordState.StopRecording -> {
                        binding.btnPauseRecording.visibility = View.GONE
                        binding.btnStopRecording.visibility = View.GONE
                        binding.btnStartRecording.visibility = View.VISIBLE
                    }
//                    is RecordState.SendRecordFile -> {
//                        sendRecord()
//                    }
                    is RecordState.Error -> {
                        binding.btnPauseRecording.visibility = View.GONE
                        binding.btnStopRecording.visibility = View.GONE
                        binding.btnStartRecording.visibility = View.VISIBLE
                        Toast.makeText(activity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun sendRecord() {
        lifecycleScope.launch {
            recordViewModel.recordIntent.send(MainIntent.SendRecordFile)
        }
    }

}