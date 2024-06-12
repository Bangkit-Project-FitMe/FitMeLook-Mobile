package com.example.fitme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.databinding.HistoryImageBinding

class HistoryImageAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<HistoryImageAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: HistoryImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResId: Int) {
            binding.imageView.setImageResource(imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}