package com.example.fitme.home.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitme.R
import com.example.fitme.databinding.ActivityAboutBinding
import com.example.fitme.databinding.ActivityNavbarBinding
import com.example.fitme.databinding.FragmentHomeBinding
import com.example.fitme.home.about.detail.DetailActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "fitMeLook"
        }

        binding.boxSummer.setOnClickListener {
            openDetailActivity("Summer")
        }

        binding.boxSpring.setOnClickListener {
            openDetailActivity("Spring")
        }

        binding.boxAutumn.setOnClickListener {
            openDetailActivity("Autumn")
        }

        binding.boxWinter.setOnClickListener {
            openDetailActivity("Winter")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    private fun openDetailActivity(season: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("SEASON", season)
        startActivity(intent)
    }
}