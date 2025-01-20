package com.example.treasurehunt

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.concurrent.thread

class ApiHandler(val url: String) {

    // START SIMPLE GET
    fun makeGetRequest() {
        // Create OkHttpClient instance
        val client = OkHttpClient()

        // Define the URL
        // val url = "https://jsonplaceholder.typicode.com/posts" // Sample API endpoint

        // Create a request object
        val request = Request.Builder()
            .url(url)
            .build()

        // Make the network call on a background thread
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
}