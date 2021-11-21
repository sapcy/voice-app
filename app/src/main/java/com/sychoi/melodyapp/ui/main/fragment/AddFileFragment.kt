package com.sychoi.melodyapp.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sychoi.melodyapp.R

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import com.sychoi.melodyapp.data.model.Voice
import com.sychoi.melodyapp.databinding.DialogAddfileBinding


class AddFileFragment : DialogFragment(R.layout.dialog_addfile) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_addfile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = DialogAddfileBinding.bind(view)

        binding.btnSave.setOnClickListener {
            val music = Voice(15, binding.etIcon.text.toString().toInt(),  binding.etTitle.text.toString(), binding.etDesc.text.toString(), binding.etPlaytime.text.toString())

        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }
}