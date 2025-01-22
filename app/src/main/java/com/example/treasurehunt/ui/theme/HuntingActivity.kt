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
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.FileOutputStream
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class HuntingActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hunting)

        previewView = findViewById(R.id.previewView)

        // Check and request camera permission
        checkCameraPermission()

        val loadImageButton = findViewById<Button>(R.id.loadImageButton)
        loadImageButton.setOnClickListener {
            val bitmap = getImageFromResources(R.drawable.tomatoes_image) // Load the image
            analyzeImageWithMLKit(bitmap) // Analyze the loaded image
        }

//        val captureButton = findViewById<Button>(R.id.captureButton)
//        captureButton.setOnClickListener {
//            capturePhoto()
//        }
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

            // Set up the preview use case
            val preview = Preview.Builder().build().also {
                it.surfaceProvider =
                    previewView.surfaceProvider  // Ensure `previewView` is initialized
            }

            // Initialize ImageCapture
            imageCapture = ImageCapture.Builder().build() // This is crucial!

            try {
                // Bind use cases to the camera
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA, // Choose the back camera
                    preview,
                    imageCapture // Bind `imageCapture` here
                )
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


//    private fun capturePhoto() {
//        val photoFile = File(
//            cacheDir,
//            "${System.currentTimeMillis()}.jpg"
//        )
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    // Once photo is saved, send it to the API
//                    sendPhotoToApi(photoFile)
//                }
//
//                override fun onError(exc: ImageCaptureException) {
//                    Toast.makeText(
//                        this@HuntingActivity,
//                        "Photo capture failed: ${exc.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Log.d("HuntingActivity", "Photo capture failed: ${exc.message}")
//                }
//            }
//        )
//    }

    private fun analyzeImageWithMLKit(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(inputImage)
            .addOnSuccessListener { labels ->
                // Iterate through the labels returned by ML Kit
                for (label in labels) {
                    val text = label.text // Label text, e.g., "Tomato"
                    val confidence = label.confidence // Confidence score
                    Log.d("MLKit", "Label: $text, Confidence: $confidence")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MLKit", "Image labeling failed: ${e.message}")
            }
    }

    private fun getImageFromResources(resourceId: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, R.drawable.tomatoes_image)
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "tomatoes_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }


//    private fun sendPhotoToApi(photoFile: File) {
//        // Create a RequestBody with the file
//        val requestBody = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
//
//        // Create a MultipartBody with the file
//        val multipartBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart(
//                "file", // The parameter name expected by the API
//                photoFile.name,
//                requestBody
//            )
//            .build()
//
//        // Use OkHttp to send the request
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("https://your-api-endpoint.com/analyze") // Replace with your API URL
//            .post(multipartBody) // Pass the multipart body
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@HuntingActivity, "Failed to send photo: ${e.message}", Toast.LENGTH_SHORT).show()
//                    Log.e("API_RESPONSE", "Error: ${e.message}")
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                runOnUiThread {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body?.string() // Get the response body
//                        Toast.makeText(this@HuntingActivity, "Photo sent successfully: $responseBody", Toast.LENGTH_SHORT).show()
//                        Log.d("API_RESPONSE", "Success: $responseBody")
//                    } else {
//                        Toast.makeText(this@HuntingActivity, "Failed to analyze photo: ${response.message}", Toast.LENGTH_SHORT).show()
//                        Log.e("API_RESPONSE", "Failure: ${response.code} - ${response.message}")
//                    }
//                }
//            }
//        })
    // }
}




