package com.example.fitme.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val season = intent.getStringExtra("SEASON")
        val imageView = findViewById<ImageView>(R.id.imgSeason)
        val textView = findViewById<TextView>(R.id.tvDesc)

        when (season) {
            "Summer" -> {
                imageView.setImageResource(R.drawable.imgsummer)
                textView.text = getString(R.string.desc_summer)
            }
            "Spring" -> {
                imageView.setImageResource(R.drawable.imgspring)
                textView.text = getString(R.string.spring)
            }
            "Autumn" -> {
                imageView.setImageResource(R.drawable.imgautumn)
                textView.text = getString(R.string.autumn)
            }
            "Winter" -> {
                imageView.setImageResource(R.drawable.imgwinter)
                textView.text = getString(R.string.winter)
            }
        }
    }
}