package com.example.treasurehunt.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurehunt.R
import com.example.treasurehunt.adapter.GridSpacingItemDecoration
import com.example.treasurehunt.adapter.ItemAdapter
import com.example.treasurehunt.adapter.Questions
import com.example.treasurehunt.model.Item

class GameSettingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var currentQuestionIndex = 0
    private val questions = listOf(Questions(
        questionText = "What is your favorite fruit?", // Question text
        inputType = "multiple_choice", // Input type
        options = listOf("Apple", "Banana", "Cherry") // Options
    ),
        Questions(
            questionText = "Enter your name:", // Question text
            inputType = "text" // Input type, no options needed for text input
        ),
        Questions(
            questionText = "What is your favorite animal?", // Question text
            inputType = "multiple_choice", // Input type
            options = listOf("Dog", "Cat", "Rabbit", "Bird") // Options
        ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_setting)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(16))

        // Initialize adapter with an empty list
        recyclerView.adapter = ItemAdapter(
            context = this,
            itemList = emptyList(),
            itemClickListener = { selectedItem ->
                Toast.makeText(this, "${selectedItem.name} selected!", Toast.LENGTH_SHORT).show()
            }
        )

        // Load the first question
        loadQuestion()

        // Handle Next button click
        findViewById<Button>(R.id.startHuntingButton).setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion()
            } else {
                // Navigate to HuntingActivity
                val intent = Intent(this, HuntingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun loadQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        when (currentQuestion.inputType) {
            "text" -> {
                findViewById<RecyclerView>(R.id.recyclerView).visibility = View.GONE
                findViewById<EditText>(R.id.userInputField).visibility = View.VISIBLE
            }
            "multiple_choice" -> {
                findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
                findViewById<EditText>(R.id.userInputField).visibility = View.GONE

                // Update RecyclerView data
                val newOptions = currentQuestion.options ?: emptyList()
                val itemList = newOptions.map { Item(it, R.drawable.tomatoes_image) }
                (recyclerView.adapter as ItemAdapter).updateData(itemList)
            }
        }
    }
}


