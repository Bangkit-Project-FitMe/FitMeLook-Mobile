package com.example.fitme.prediction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.fitme.R
import com.example.fitme.adapter.ColorPaletteAdapter
import com.example.fitme.adapter.HistoryImageAdapter
import com.example.fitme.adapter.ResultImageAdapter
import com.example.fitme.databinding.ActivityResultBinding
import com.example.fitme.home.MainActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var skinTone: String
    private lateinit var faceShape: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        skinTone = "Winter"

        setUpBinding()
        setUpDescription()
        setUpRecommendedColors()
        setUpAvoidedColors()

        val rvResult: RecyclerView = binding.recyclerView
        val gridlayoutManager = GridLayoutManager(this, 2)
        rvResult.layoutManager = gridlayoutManager
        val imageList = listOf(0, 0, 0, 0, 0)
        rvResult.adapter = ResultImageAdapter(imageList)

    }

    private fun setUpBinding() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpDescription() {
        val descResId = when (skinTone) {
            "Summer" -> R.string.summerDesc
            "Autumn" -> R.string.autumnDesc
            "Winter" -> R.string.winterDesc
            else -> R.string.springDesc
        }
        val desc = getString(descResId)
        binding.textDescription.text = desc

        val descResInfo = when (skinTone) {
            "Summer" -> R.string.summerInfo
            "Autumn" -> R.string.autumnInfo
            "Winter" -> R.string.winterInfo
            else -> R.string.springInfo
        }
        val descInfo = getString(descResInfo)
        binding.textDetail.text = descInfo
    }

    private fun setUpRecommendedColors() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvRecommendedColor: RecyclerView = binding.rvRecommended
        rvRecommendedColor.layoutManager = layoutManager

        val recommendedColorList = when (skinTone) {
            "Summer" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            "Autumn" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            "Winter" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            else -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
        }

        rvRecommendedColor.adapter = ColorPaletteAdapter(recommendedColorList)
    }

    private fun setUpAvoidedColors() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvAvoidedColor: RecyclerView = binding.rvAvoided
        rvAvoidedColor.layoutManager = layoutManager

        val avoidedColorList = when (skinTone) {
            "Summer" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            "Autumn" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            "Winter" -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
            else -> listOf("#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8", "#E8E8E8")
        }

        rvAvoidedColor.adapter = ColorPaletteAdapter(avoidedColorList)
    }

}