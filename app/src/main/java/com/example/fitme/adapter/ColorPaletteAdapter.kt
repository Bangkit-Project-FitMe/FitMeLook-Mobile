package com.example.fitme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.databinding.ItemPaletteBinding

class ColorPaletteAdapter(private val colorList: List<String>) : RecyclerView.Adapter<ColorPaletteAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(private val binding: ItemPaletteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color: String) {
            binding.palette.setCardBackgroundColor(android.graphics.Color.parseColor(color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemPaletteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}
