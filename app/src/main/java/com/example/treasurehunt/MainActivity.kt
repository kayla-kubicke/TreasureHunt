package com.example.treasurehunt

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)



        button?.setOnClickListener()
        {
            // Button
                // strings.xml - Message popup
                // activity_main.xml - Physical properties - size, color, etc.
            Toast.makeText(this@MainActivity, R.string.message, Toast.LENGTH_LONG).show()

            val handler = ApiHandler("https://jsonplaceholder.typicode.com/posts")
            handler.makeGetRequest()
        }
    }
}
