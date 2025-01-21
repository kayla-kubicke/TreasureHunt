package com.example.treasurehunt.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurehunt.R
import com.example.treasurehunt.adapter.GridSpacingItemDecoration
import com.example.treasurehunt.adapter.ItemAdapter
import com.example.treasurehunt.model.Item

class GameSettingActivity : AppCompatActivity() {
    private val selectedItems = mutableListOf<Item>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_setting)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        recyclerView.addItemDecoration(GridSpacingItemDecoration(16)) // 16dp spacing

        // Create list of items
        val itemList: MutableList<Item> = ArrayList()
        itemList.add(Item("Tomatoes", R.drawable.tomatoes_image))
        itemList.add(Item("Apples", R.drawable.tomatoes_image))
        itemList.add(Item("Tomatoes", R.drawable.tomatoes_image))
        itemList.add(Item("Fruits", R.drawable.tomatoes_image))

        // Set adapter
        // Set adapter with click listener
        val adapter = ItemAdapter(this, itemList) { selectedItem ->
            // Handle item click
            if (!selectedItems.contains(selectedItem)) {
                selectedItems.add(selectedItem) // Add to selected list
                Toast.makeText(this, "${selectedItem.name} selected", Toast.LENGTH_SHORT).show()
            } else {
                selectedItems.remove(selectedItem) // Remove from selected list if already added
                Toast.makeText(this, "${selectedItem.name} deselected", Toast.LENGTH_SHORT).show()
            }
        }
//        val adapter = ItemAdapter(this, itemList)
        recyclerView.adapter = adapter
        // Find the button and set an OnClickListener
        val startHuntingButton = findViewById<Button>(R.id.startHuntingButton)
        startHuntingButton.setOnClickListener {
            // Navigate to HuntingActivity
            val intent = Intent(this, HuntingActivity::class.java)
            startActivity(intent)
        }
    }
}

