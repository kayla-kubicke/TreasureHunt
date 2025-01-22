package com.example.treasurehunt.ui.theme

import android.Manifest
import android.content.Intent
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.treasurehunt.model.Hint

import java.io.FileOutputStream
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class HuntingActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var hintsContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hunting)

        previewView = findViewById(R.id.previewView)
        hintsContainer = findViewById(R.id.hintsContainer)
        // Add hints dynamically
        val hints = listOf(
            Hint(R.drawable.tomatoes_image, "I turned black, so people don't like me."),
            Hint(R.drawable.tomatoes_image, "Sprouts are growing on my body, so people don't like me."),
            Hint(R.drawable.tomatoes_image, "I'm too green, so nobody wants me.")
        )
        updateHints(hints)
        // Check and request camera permission
        checkCameraPermission()
        requestCameraPermission()

        val loadImageButton = findViewById<Button>(R.id.loadImageButton)
//        loadImageButton.setOnClickListener {
//            val bitmap = getImageFromResources(R.drawable.tomatoes_image) // Load the image
//            analyzeImageWithMLKit(bitmap) // Analyze the loaded image
//        }
//        loadImageButton.setOnClickListener {
//            val imageResource = R.drawable.tomatoes_image // The image you're analyzing
//            val bitmap = getImageFromResources(imageResource) // Load the image from resources
//            val correctLabels = imageLabels[imageResource] ?: listOf() // Fetch the acceptable labels list
//
//            analyzeImageWithMLKit(bitmap, correctLabels) // Pass the list of acceptable labels
//        }

        val captureButton = findViewById<Button>(R.id.captureButton)
        captureButton.setOnClickListener {
            capturePhoto()
        }
    }
    private fun updateHints(hints: List<Hint>) {
        hintsContainer.removeAllViews() // Clear previous hints

        for (hint in hints) {
            val hintView = layoutInflater.inflate(R.layout.hint_item, hintsContainer, false)

            val imageView = hintView.findViewById<ImageView>(R.id.hintImage)
            val textView = hintView.findViewById<TextView>(R.id.hintText)

            imageView.setImageResource(hint.imageRes)
            textView.text = hint.text

            hintsContainer.addView(hintView)
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

    private fun capturePhoto() {
        val photoFile = File(cacheDir, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Decode the saved image into a Bitmap
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    Log.d("CameraX", "Photo captured and saved to: ${photoFile.absolutePath}")

                    if (bitmap != null) {
                        // Analyze the captured bitmap
                        //val correctLabels = listOf("Tomato", "Vegetable", "Food", "Plant")
                        // Pass the bitmap to Firebase ML Kit for processing
                       // analyzeImageWithMLKit(bitmap, listOf("Tomato","Vegetable","Food","Plant")  )
                        val correctLabels = listOf("Tomato", "Vegetable", "Food", "Plant","Human","Smile","Fast food")
                        analyzeImageWithMLKit(bitmap, correctLabels)
                    } else {
                        Log.e("CameraX", "Failed to decode captured image.")
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exc.message}")
                }
            }
        )
    }


//    private val imageLabels = mapOf(
//        R.drawable.tomatoes_image to listOf("Tomato", "Vegetable", "Food", "Plant"),
////        "Apple" to listOf("Apple", "Fruit"),
////        "Banana" to listOf("Banana", "Fruit")
//    )
//    val correctLabel = imageLabels[R.drawable.tomatoes_image] ?: ""
    private fun navigateToNextPage() {
        val intent = Intent(this, GameSettingActivity::class.java)
        startActivity(intent)
    }

    private val confidenceThreshold = 0.5f // Adjust as needed
    private fun analyzeImageWithMLKit(bitmap: Bitmap, correctLabels: List<String>) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        Log.d("MLKit", "Starting image analysis...")

        labeler.process(inputImage)
            .addOnSuccessListener { labels ->
                Log.d("MLKit", "Image analysis successful. Labels received:")
                for (label in labels) {
                    Log.d("MLKit", "Predicted label: ${label.text}, Confidence: ${label.confidence}")
                }

                val matchFound = labels.any { label ->
                    val isMatch = correctLabels.contains(label.text)
                    Log.d("MLKit", "Checking label: ${label.text}. Match found: $isMatch")
                    isMatch
                }

                if (matchFound) {
                    Toast.makeText(this, "Success! Moving to the next level.", Toast.LENGTH_SHORT).show()
                    navigateToNextPage()
                } else {
                    Toast.makeText(this, "No match found. Try again!", Toast.LENGTH_SHORT).show()
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

}




