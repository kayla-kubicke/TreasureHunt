package com.example.treasurehunt

import android.Manifest
import android.Manifest.permission.CAMERA

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.camera.core.Camera
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurehunt.adapter.ItemAdapter
import com.example.treasurehunt.model.Item


//import android.content.Intent
//
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.tasks.Task
//// Keep for now.
//import com.example.treasurehunt.ui.theme.TreasureHuntTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//this is the Runtime request permission for the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }
//        @Override
//        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//            if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
//            }
//        }

//        val camera: Camera = CAMERA.open()
//        val preview: CameraPreview = CameraPreview(this, camera)
//        val previewLayout = findViewById<FrameLayout>(R.id.camera_preview)
//        previewLayout.addView(preview)
//
//        captureButton.setOnClickListener { v ->
//            camera.takePicture(null, null) { data, camera -> }
//        }
//        val button = findViewById<Button>(R.id.button)
//
//        button?.setOnClickListener()
        {
            // Button
                // strings.xml - Message popup
                // activity_main.xml - Physical properties - size, color, etc.
//            Toast.makeText(this@MainActivity, R.string.message, Toast.LENGTH_LONG).show()


            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns

            val itemList: MutableList<Item> = ArrayList<Item>()
          itemList.add(Item("tomatoes", R.drawable.tomatoes_image))
            itemList.add(Item("apples", R.drawable.tomatoes_image))
          itemList.add(Item("tomatoes", R.drawable.tomatoes_image))
          itemList.add(Item("fruits", R.drawable.tomatoes_image))


            // Add more items as needed
            val adapter = ItemAdapter(this, itemList)
            recyclerView.adapter = adapter
        }
    }
}

