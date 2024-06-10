package com.example.fitme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.databinding.ItemResultBinding

class ResultImageAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<ResultImageAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ItemResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResId: Int) {
            binding.imageView.setImageResource(imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}
