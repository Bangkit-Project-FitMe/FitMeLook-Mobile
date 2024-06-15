package com.example.fitme.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.R
import com.example.fitme.api.response.ListHistoryData
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*


class ItemDayAdapter(private val dayList: List<ListHistoryData>) : RecyclerView.Adapter<ItemDayAdapter.ItemDayViewHolder>() {

    class ItemDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
        val rvImages: RecyclerView = view.findViewById(R.id.rvImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return ItemDayViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemDayViewHolder, position: Int) {
        val dayItem = dayList[position]

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dayItem.created_at)

        val currentDate = Calendar.getInstance().time
        val yesterdayDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = when {
            DateUtils.isToday(date.time) -> "Today"
            DateUtils.isToday(yesterdayDate.time) -> "Yesterday"
            else -> outputFormat.format(date)
        }

        holder.tvDay.text = "$formattedDate | ${dayItem.seasonal_type}"
        holder.rvImages.layoutManager = LinearLayoutManager(holder.rvImages.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvImages.adapter = ItemImageAdapter(dayItem.response_images)
    }



    override fun getItemCount(): Int = dayList.size
}
