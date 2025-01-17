package com.example.treasurehunt

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
// Keep for now.
import com.example.treasurehunt.ui.theme.TreasureHuntTheme


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
        }
    }
}

