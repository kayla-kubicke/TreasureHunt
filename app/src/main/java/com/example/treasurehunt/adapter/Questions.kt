package com.example.treasurehunt.adapter
//data class to handle the format of the game setting questions
data class Questions (
    val questionText: String,
    val inputType: String, // Example: "text", "multiple_choice"
    val options: List<String>? = null // For multiple-choice questions
)