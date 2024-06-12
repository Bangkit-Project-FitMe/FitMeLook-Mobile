package com.example.fitme.prediction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.databinding.ActivityProcessBinding

class ProcessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcessBinding
    private val splashTimeout: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout)
    }
}