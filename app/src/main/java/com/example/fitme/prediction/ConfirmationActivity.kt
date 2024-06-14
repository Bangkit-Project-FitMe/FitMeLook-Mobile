package com.example.fitme.prediction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fitme.ViewModelFactory
import com.example.fitme.api.ResultState
import com.example.fitme.databinding.ActivityConfirmationBinding
import com.example.fitme.home.MainActivity
import com.example.fitme.prediction.model.PredictionModel
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding
    private var currentImageFile: File? = null
    private val viewModel by viewModels<PredictionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentImageFile = intent.getSerializableExtra(ConfirmationActivity.EXTRA_IMAGE_FILE) as File?
        if (currentImageFile != null && currentImageFile!!.exists()) {
            Glide.with(this)
                .load(currentImageFile)
                .centerCrop()
                .into(binding.imageView)
        } else {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.buttonRetake.setOnClickListener { handleCancel() }
        handleAnalyze()
    }

    private fun handleCancel() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun handleAnalyze() {
        binding.buttonAnalyze.setOnClickListener {
            currentImageFile?.let { uri ->
                val userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                viewModel.predict(userID, currentImageFile!!).observe(this) { predict ->
                    when (predict) {
                        is ResultState.Success -> {
                            val predictionModel =  PredictionModel(
                                predict.data.data.faceShape,
                                predict.data.data.seasonalType,
                                predict.data.data.faceShapeConfidenceScore,
                                predict.data.data.seasonalTypeConfidenceScore,
                                predict.data.data.createdAt,
                                predict.data.data.responseImages,
                                predict.data.data.imageUrl
                            )
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(ResultActivity.EXTRA_PREDICTION_MODEL, predictionModel)
                            startActivity(intent)
                            finish()
                        }

                        is ResultState.Loading -> {
                            binding.buttonAnalyze.visibility = View.GONE
                            binding.buttonRetake.visibility = View.GONE

                            binding.textGreat.visibility = View.VISIBLE
                            binding.textWait.visibility = View.VISIBLE
                            binding.textAnalyzing.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is ResultState.Error -> {
                            Toast.makeText(this, predict.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE_FILE = ""
    }
}