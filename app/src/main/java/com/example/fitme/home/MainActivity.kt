package com.example.fitme.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fitme.login.LoginActivity
import com.example.fitme.databinding.ActivityMainBinding
import com.example.fitme.ViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnMap.setOnClickListener {
            showLogoutConfirmationDialog()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
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

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Logout") { dialogInterface: DialogInterface, i: Int ->
            signOut()
        }
        builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> }
        val dialog = builder.create()
        dialog.show()
    }

    private fun signOut() {
        auth.signOut()
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            updateUI()
        }
    }

    private fun updateUI() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
    }

}