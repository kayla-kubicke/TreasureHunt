package com.example.treasurehunt.adapter

//import android.R
import com.example.treasurehunt.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.treasurehunt.model.Item


class ItemAdapter(context: Context?, itemList: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val itemList: List<Item> = itemList
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = inflater.inflate(R.layout.grid_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem: Item = itemList[position]
        holder.itemText.setText(currentItem.name)
        holder.itemImage.setImageResource(currentItem.imageResId)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById<ImageView>(R.id.itemImage)
        val itemText: TextView = itemView.findViewById<TextView>(R.id.itemText)
    }
}
