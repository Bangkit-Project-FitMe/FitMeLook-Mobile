package com.example.fitme.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.login.LoginActivity
import com.example.fitme.databinding.ActivityMainBinding
import com.example.fitme.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()
    }

    private fun checkLogin() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin){
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Already Login!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}