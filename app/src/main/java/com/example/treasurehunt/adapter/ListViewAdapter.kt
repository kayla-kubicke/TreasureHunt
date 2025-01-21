

package com.example.treasurehunt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurehunt.R
import com.example.treasurehunt.model.Item // Import the Item class
//
//class ItemAdapter(
//    private val context: Context,
//    private val itemList: List<Item>,
//    private val itemClickListener: (Item) -> Unit // Lambda for item clicks
//) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val currentItem = itemList[position]
//        holder.itemText.text = currentItem.name
//        holder.itemImage.setImageResource(currentItem.imageResId)
//
//        holder.itemView.setOnClickListener {
//            itemClickListener(currentItem)
//        }
//    }
//
//    override fun getItemCount(): Int = itemList.size
//
//    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
//        val itemText: TextView = itemView.findViewById(R.id.itemText)
//    }
//}
class ItemAdapter(
    private val context: Context,
    private val itemList: List<Item>,
    private val itemClickListener: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemText.text = currentItem.name
        holder.itemImage.setImageResource(currentItem.imageResId)

        // Add click listener for the item
        holder.itemView.setOnClickListener {
            itemClickListener(currentItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemText: TextView = itemView.findViewById(R.id.itemText)
    }
}
