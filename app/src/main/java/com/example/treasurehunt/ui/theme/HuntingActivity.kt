package com.example.treasurehunt.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.treasurehunt.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class HuntingActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hunting)

        previewView = findViewById(R.id.previewView)

        // Check and request camera permission
        checkCameraPermission()
        // Set up the capture button
        val captureButton = findViewById<Button>(R.id.captureButton)
        captureButton.setOnClickListener {
            capturePhoto()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission()
        } else {
            startCamera()
        }
    }

    private fun requestCameraPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider

//                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("HuntingActivity", "Camera binding failed: ${exc.message}")
                Toast.makeText(this, "Failed to bind camera use cases", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun capturePhoto() {
        val photoFile = File(
            cacheDir,
            "${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Once photo is saved, send it to the API
                    sendPhotoToApi(photoFile)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@HuntingActivity, "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun sendPhotoToApi(photoFile: File) {
        // Create a RequestBody with the file
        val requestBody = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBodyPart = MultipartBody.Part.createFormData(
            "file", // The parameter name expected by the API
            photoFile.name,
            requestBody
        )

        // Create a MultipartBody containing the part
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(multipartBodyPart)
            .build()

        // Use the MultipartBody with the Request.Builder
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://your-api-endpoint.com/analyze") // Replace with your API URL
            .post(multipartBody) // Use the MultipartBody here
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@HuntingActivity, "Failed to send photo: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@HuntingActivity, "Photo sent successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@HuntingActivity, "Failed to analyze photo: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


