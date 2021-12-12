package com.sychoi.melodyapp.ui.main.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sychoi.melodyapp.R

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.sychoi.melodyapp.data.api.ApiHelperImpl
import com.sychoi.melodyapp.data.api.RetrofitBuilder
import com.sychoi.melodyapp.data.model.UploadRequestBody
import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.databinding.DialogAddfileBinding
import com.sychoi.melodyapp.ui.main.view.MainActivity
import com.sychoi.melodyapp.util.getFileName
import com.sychoi.melodyapp.util.snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@ExperimentalCoroutinesApi
class AddFileFragment : DialogFragment(R.layout.dialog_addfile), UploadRequestBody.UploadCallback {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: DialogAddfileBinding
    private var selectedFileUri: Uri? = null
    private val apiHelper = ApiHelperImpl(RetrofitBuilder.apiService)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_addfile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogAddfileBinding.bind(view)

        binding.ivTabFile.setOnClickListener {
            openImageChooser()
        }

        binding.btnSave.setOnClickListener {
            uploadFile()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun openImageChooser() {
        Intent().also {
            it.type = "audio/*"
            it.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(it, "Get Image"), REQUEST_CODE_PICK_AUDIO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_AUDIO -> {
                    selectedFileUri = data?.data
                }
            }
        }
    }

    private fun uploadFile() {
        if (selectedFileUri == null) {
            binding.dialogAddfile.snackbar("Select an File First")
            return
        }

        val parcelFileDescriptor =
            mainActivity.contentResolver.openFileDescriptor(selectedFileUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(mainActivity.cacheDir, mainActivity.contentResolver.getFileName(selectedFileUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val filebody = UploadRequestBody(file, "audio", this)

        val jsonObject = JSONObject()
        jsonObject.put("title", binding.etTitle.text)
        jsonObject.put("desc", binding.etDesc.text)
        jsonObject.put("playtime", binding.etPlaytime.text)

        val fields: HashMap<String?, RequestBody?> = HashMap()
//        fields["file"] = filebody
        fields["voice"] = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())


        lifecycleScope.launch {
            apiHelper.upload(
                fields,
                MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    filebody
                ),
                "json".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            ).enqueue(object : Callback<UploadResponse> {
                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    binding.dialogAddfile.snackbar(t.message!!)
                    binding.progressBar.progress = 0
                }

                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    response.body()?.let {
                        binding.dialogAddfile.snackbar(it.message)
                        binding.progressBar.progress = 100
                    }
                }
            })
        }

    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_AUDIO = 101
    }
}