package com.example.fitme.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitme.R

class ItemImageAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<ItemImageAdapter.ItemImageViewHolder>() {

    class ItemImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ItemImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Log.d("ItemImageAdapter", "Loading image from URL: $imageUrl")

        Glide.with(holder.imageView.context)
            .load(transform(imageUrl))
            .placeholder(R.color.white)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)
    }

    private fun transform(url: String): String {
        if (url.startsWith("https://storage.cloud.google.com/")) {
            return url.replace("https://storage.cloud.google.com/", "https://storage.googleapis.com/")
        }
        return url
    }


    override fun getItemCount(): Int = imageUrls.size
}
