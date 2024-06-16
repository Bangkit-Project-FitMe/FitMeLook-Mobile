package com.example.fitme.prediction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitme.ViewModelFactory
import com.example.fitme.api.ResultState
import com.example.fitme.databinding.ActivityConfirmationBinding
import com.example.fitme.home.MainActivity
import com.example.fitme.prediction.model.PredictionModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
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
        Log.d("ConfirmationActivity", "Current image file: ${currentImageFile?.absolutePath}")
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
            lifecycleScope.launch {
                currentImageFile?.let { uri ->
                    Log.d("ConfirmationActivity", "Analyzing file: ${uri?.absolutePath}")
                    val userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    viewModel.predict(userID, uri!!).observe(this@ConfirmationActivity) { predict ->
                        when (predict) {
                            is ResultState.Success -> {
                                Toast.makeText(this@ConfirmationActivity, predict.data.message, Toast.LENGTH_SHORT).show()
                                if (predict.data.data.responseImages.isEmpty()) {
                                    val intent = Intent(this@ConfirmationActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val predictionModel = PredictionModel(
                                        predict.data.data.faceShape ?: "",
                                        predict.data.data.seasonalType ?: "",
                                        predict.data.data.faceShapeConfidenceScore ?: 0.0,
                                        predict.data.data.seasonalTypeConfidenceScore ?: 0.0,
                                        predict.data.data.createdAt,
                                        predict.data.data.responseImages,
                                        predict.data.data.imageUrl
                                    )
                                    val intent = Intent(this@ConfirmationActivity, ResultActivity::class.java)
                                    intent.putExtra(
                                        ResultActivity.EXTRA_PREDICTION_MODEL,
                                        predictionModel
                                    )
                                    startActivity(intent)
                                    finish()
                                }
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
                                Toast.makeText(this@ConfirmationActivity, predict.error, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ConfirmationActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
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