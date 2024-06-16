package com.example.fitme.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.R
import com.example.fitme.api.response.ListHistoryData
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*


class ItemDayAdapter(private val dayList: List<ListHistoryData>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<ItemDayAdapter.ItemDayViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(historyData: ListHistoryData)
    }

    class ItemDayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
        val rvImages: RecyclerView = view.findViewById(R.id.rvImages)
        val cardView: CardView = view.findViewById(R.id.cardView)
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
        val calendar = Calendar.getInstance()

        calendar.time = currentDate
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time = date
        val itemYear = calendar.get(Calendar.YEAR)
        val itemMonth = calendar.get(Calendar.MONTH)
        val itemDay = calendar.get(Calendar.DAY_OF_MONTH)

        val isToday = currentYear == itemYear && currentMonth == itemMonth && currentDay == itemDay
        val isYesterday = currentYear == itemYear && currentMonth == itemMonth && currentDay - itemDay == 1

        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = when {
            isToday -> "Today"
            isYesterday -> "Yesterday"
            else -> outputFormat.format(date)
        }

        holder.tvDay.text = "$formattedDate | ${dayItem.seasonal_type}"
        holder.rvImages.layoutManager = LinearLayoutManager(holder.rvImages.context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvImages.adapter = ItemImageAdapter(dayItem.response_images.take(6))

        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(dayItem)
        }
    }

    override fun getItemCount(): Int = dayList.size
}
