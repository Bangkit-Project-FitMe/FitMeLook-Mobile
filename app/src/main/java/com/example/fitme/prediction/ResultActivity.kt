package com.example.fitme.prediction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.fitme.adapter.ColorPaletteAdapter
import com.example.fitme.adapter.HistoryImageAdapter
import com.example.fitme.adapter.ResultImageAdapter
import com.example.fitme.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvResult: RecyclerView = binding.recyclerView
        val gridlayoutManager = GridLayoutManager(this, 2)
        rvResult.layoutManager = gridlayoutManager
        val imageList = listOf(0, 0, 0, 0, 0)
        rvResult.adapter = ResultImageAdapter(imageList)

        val rvColor: RecyclerView = binding.rvPalette
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvColor.layoutManager = layoutManager
        val colorList = listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
        rvColor.adapter = ColorPaletteAdapter(colorList)
    }

}