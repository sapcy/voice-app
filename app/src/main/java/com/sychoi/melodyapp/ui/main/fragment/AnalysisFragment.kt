package com.sychoi.melodyapp.ui.main.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.data.api.ApiHelperImpl
import com.sychoi.melodyapp.data.api.RetrofitBuilder
import com.sychoi.melodyapp.data.model.UploadRequestBody
import com.sychoi.melodyapp.data.model.UploadResponse
import com.sychoi.melodyapp.databinding.FragmentAnalysisBinding
import com.sychoi.melodyapp.ui.main.view.MainActivity
import com.sychoi.melodyapp.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

//@ExperimentalCoroutinesApi
class AnalysisFragment : Fragment(R.layout.fragment_analysis), UploadRequestBody.UploadCallback {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentAnalysisBinding
    private var selectedImageUri: Uri? = null
    private val apiHelper = ApiHelperImpl(RetrofitBuilder.apiService)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAnalysisBinding.bind(view)

        binding.ivTabImage .setOnClickListener {
            openImageChooser()
        }

        binding.btnUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun openImageChooser() {
        Intent().also {
//            it.type = "image/*"
            it.type = "audio/*"
            it.action = Intent.ACTION_GET_CONTENT
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(Intent.createChooser(it, "Get Image"), REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.ivTabImage.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadFile() {
        if (selectedImageUri == null) {
            binding.fragmentAnalysis.snackbar("Select an File First")
            return
        }

        val parcelFileDescriptor =
            mainActivity.contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(mainActivity.cacheDir, mainActivity.contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "audio", this)
        apiHelper.upload(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.fragmentAnalysis.snackbar(t.message!!)
                binding.progressBar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    binding.fragmentAnalysis.snackbar(it.message)
                    binding.progressBar.progress = 100
                }
            }
        })

    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
}