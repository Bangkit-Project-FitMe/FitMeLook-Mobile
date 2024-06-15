package com.example.fitme.prediction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.fitme.R
import com.example.fitme.ViewModelFactory
import com.example.fitme.adapter.ColorPaletteAdapter
import com.example.fitme.adapter.ResultImageAdapter
import com.example.fitme.databinding.ActivityResultBinding
import com.example.fitme.home.MainActivity
import com.example.fitme.login.LoginViewModel
import com.example.fitme.prediction.model.PredictionModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var skinTone: String
    private lateinit var faceShape: String
    private val viewModel by viewModels<PredictionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val predictionModel = intent.getParcelableExtra<PredictionModel>(EXTRA_PREDICTION_MODEL)

        predictionModel?.let { model ->
            skinTone = model.seasonalType
            faceShape = model.faceShape

            setUpBinding()
            setUpDescription()
            setUpRecommendedColors()
            setUpAvoidedColors()

            val rvResult: RecyclerView = binding.recyclerView
            val gridlayoutManager = GridLayoutManager(this, 2)
            rvResult.layoutManager = gridlayoutManager
            val imageList = predictionModel.responseImages.take(6)
            rvResult.adapter = ResultImageAdapter(imageList)

            viewModel.getSession().observe(this) { session ->
                session?.let {
                    val fullName = session.fullName.split(" ").firstOrNull() ?: ""
                    val greetingText = getString(R.string.greeting, fullName)
                    binding.textGreeting.text = greetingText
                }
            }

            Glide.with(this)
                .load(predictionModel.imageUrl)
                .transform(CircleCrop())
                .into(binding.imageView)

        } ?: run {
            Toast.makeText(this, "Prediction model is null", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setUpBinding() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.removeExtra(ResultActivity.EXTRA_PREDICTION_MODEL)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpDescription() {
        binding.textSeason.text = skinTone
        Log.d("Result", skinTone)
        binding.textFace.text = faceShape

        val photoResId = when (skinTone) {
            "Summer" -> R.drawable.summer
            "Autumn" -> R.drawable.autumn
            "Winter" -> R.drawable.winter
            else -> R.drawable.spring
        }

        Glide.with(this)
            .load(photoResId)
            .centerCrop()
            .into(binding.imageIcon)

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

        val colorResId = when (skinTone) {
            "Summer" -> R.color.summer
            "Autumn" -> R.color.autumn
            "Winter" -> R.color.winter
            else -> R.color.spring
        }
        val color = ContextCompat.getColor(this, colorResId)
        binding.cardRoundedBox.setCardBackgroundColor(color)
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

    companion object {
        const val EXTRA_PREDICTION_MODEL = "EXTRA_PREDICTION_MODEL"
    }

}