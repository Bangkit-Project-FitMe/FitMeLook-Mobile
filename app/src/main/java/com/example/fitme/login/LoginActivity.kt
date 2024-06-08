package com.example.fitme.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.databinding.ActivityLoginBinding
import com.example.fitme.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupBinding()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupBinding() {
        binding.loginToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
        }

        binding.googleLoginButton.setOnClickListener {
            Toast.makeText(this, "Login Google", Toast.LENGTH_SHORT).show()
        }
    }

}