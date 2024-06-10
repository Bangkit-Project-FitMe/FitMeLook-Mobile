package com.example.fitme.prediction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.databinding.ActivityConfirmationBinding
import com.example.fitme.home.MainActivity

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        currentImageUri = Uri.parse(imageUriString)


        binding.imageView.setImageURI(currentImageUri)
        binding.buttonRetake.setOnClickListener { handleRetake() }
        binding.buttonAnalyze.setOnClickListener { handleAnalyze() }
    }

    private fun handleRetake() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun handleAnalyze() {
        val intent = Intent(this, ProcessActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_IMAGE_URI = ""
    }
}