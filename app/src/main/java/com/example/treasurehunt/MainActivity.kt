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

        // START SIMPLE GET
        fun makeGetRequest() {
            // Create OkHttpClient instance
            val client = OkHttpClient()

            // Define the URL
            val url = "https://jsonplaceholder.typicode.com/posts" // Sample API endpoint
            // Create a request object
            val request = Request.Builder()
                .url(url)
                .build()

            // Make the network call on a background thread
            // NOTE: Need to move into separate class.
            thread {
                try {
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        // Check if the request was successful
                        if (response.isSuccessful) {
                            // Get the response body
                            val responseBody = response.body?.string()
                            Log.d("JSON: ", "$responseBody")

                            // START: JSON?
                            // val jsonObject: JSONObject = JSONObject(responseBody)
                            // Log.d("JSON: ", "JSON: $jsonObject")
                            // END: JSON?
                        } else {
                            println("Error: ${response.code}")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        // END SIMPLE GET

        button?.setOnClickListener()
        {
            // Button
                // strings.xml - Message popup
                // activity_main.xml - Physical properties - size, color, etc.
            Toast.makeText(this@MainActivity, R.string.message, Toast.LENGTH_LONG).show()

            makeGetRequest()
        }
    }
}
