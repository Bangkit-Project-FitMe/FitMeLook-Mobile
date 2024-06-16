package com.example.fitme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.databinding.ItemResultBinding
import com.bumptech.glide.Glide
import com.example.fitme.R

class ResultImageAdapter(private val imageUrlList: List<String>) : RecyclerView.Adapter<ResultImageAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(itemView.context)
                .load(transform(imageUrl))
                .placeholder(R.color.white)
                .into(binding.imageView)
        }
    }

    private fun transform(url: String): String {
        if (url.startsWith("https://storage.cloud.google.com/")) {
            return url.replace("https://storage.cloud.google.com/", "https://storage.googleapis.com/")
        }
        return url
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }
}
