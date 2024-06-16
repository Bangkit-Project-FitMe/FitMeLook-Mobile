package com.example.fitme.prediction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.fitme.R
import com.example.fitme.ViewModelFactory
import com.example.fitme.adapter.ColorPaletteAdapter
import com.example.fitme.adapter.ResultImageAdapter
import com.example.fitme.api.ApiConfig
import com.example.fitme.api.ApiService
import com.example.fitme.api.response.DetailHistoryData
import com.example.fitme.databinding.ActivityResultBinding
import com.example.fitme.home.MainActivity
import com.example.fitme.prediction.model.PredictionModel
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var userId: String
    private lateinit var predictionId: String
    private lateinit var apiService: ApiService
    private val viewModel by viewModels<PredictionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = ApiConfig.getApiService()

        val fromHistory = intent.getBooleanExtra("FROM_HISTORY", false)

        if (fromHistory) {
            userId = intent.getStringExtra("USER_ID") ?: return
            predictionId = intent.getStringExtra("PREDICTION_ID") ?: return
            fetchDetailHistory()
        } else {
            val predictionModel = intent.getParcelableExtra<PredictionModel>(EXTRA_PREDICTION_MODEL)
            predictionModel?.let { model ->
                displayPredictionResult(model)
            } ?: run {
                Toast.makeText(this, "Prediction model is null", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun fetchDetailHistory() {
        lifecycleScope.launch {
            try {
                val response = apiService.getDetailHistory(userId, predictionId)
                val detailHistoryData = response.data
                displayDetailHistory(detailHistoryData)
            } catch (e: Exception) {
                Log.e("ResultActivity", "Failed to fetch detail history", e)
                Toast.makeText(this@ResultActivity, "Failed to load history data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayDetailHistory(data: DetailHistoryData) {
        binding.textSeason.text = data.seasonal_type
        binding.textFace.text = data.face_shape

        Glide.with(this)
            .load(data.image_url)
            .transform(CircleCrop())
            .into(binding.imageView)

        val rvResult: RecyclerView = binding.recyclerView
        val gridLayoutManager = GridLayoutManager(this, 2)
        rvResult.layoutManager = gridLayoutManager
        rvResult.adapter = ResultImageAdapter(data.response_images.take(6))
        setUpBinding()
        setUpDescription(data.seasonal_type, data.face_shape)
        setUpRecommendedColors(data.seasonal_type)
        setUpAvoidedColors(data.seasonal_type)

        viewModel.getSession().observe(this) { session ->
            session?.let {
                val fullName = session.fullName.split(" ").firstOrNull() ?: ""
                val greetingText = getString(R.string.greeting, fullName)
                binding.textGreeting.text = greetingText
            }
        }
    }

    private fun displayPredictionResult(model: PredictionModel) {
        binding.textSeason.text = model.seasonalType
        binding.textFace.text = model.faceShape

        Glide.with(this)
            .load(model.imageUrl)
            .placeholder(R.color.white)
            .transform(CircleCrop())
            .into(binding.imageView)

        val rvResult: RecyclerView = binding.recyclerView
        val gridLayoutManager = GridLayoutManager(this, 2)
        rvResult.layoutManager = gridLayoutManager
        rvResult.adapter = ResultImageAdapter(model.responseImages.take(6))

        setUpBinding()
        setUpDescription(model.seasonalType, model.faceShape)
        setUpRecommendedColors(model.seasonalType)
        setUpAvoidedColors(model.seasonalType)

        viewModel.getSession().observe(this) { session ->
            session?.let {
                val fullName = session.fullName.split(" ").firstOrNull() ?: ""
                val greetingText = getString(R.string.greeting, fullName)
                binding.textGreeting.text = greetingText
            }
        }
    }

    private fun setUpBinding() {
        binding.btnBack.setOnClickListener {
            val fromHistory = intent.getBooleanExtra("FROM_HISTORY", false)
            if (fromHistory) {
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.removeExtra(ResultActivity.EXTRA_PREDICTION_MODEL)
                finish()
            }
            finish()
        }
    }


    private fun setUpDescription(skinTone: String, faceShape: String) {
        binding.textSeason.text = skinTone
        binding.textFace.text = faceShape

        val about: String

        val descResId = when (skinTone) {
            "Summer" -> R.string.summerDesc
            "Autumn" -> R.string.autumnDesc
            "Winter" -> R.string.winterDesc
            else -> R.string.springDesc
        }
        val desc = getString(descResId)

        val descFaceId = when (faceShape) {
            "Oblong" -> R.string.oblongDesc
            "Heart" -> R.string.heartDesc
            "Round" -> R.string.roundDesc
            else -> R.string.squareDesc
        }
        val descFace = getString(descFaceId)

        about = "$desc $descFace"

        binding.textDescription.text = about

        val colorResId = when (skinTone) {
            "Summer" -> R.color.summer
            "Autumn" -> R.color.autumn
            "Winter" -> R.color.winter
            else -> R.color.spring
        }
        val color = ContextCompat.getColor(this, colorResId)
        binding.cardRoundedBox.setCardBackgroundColor(color)

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
    }

    private fun setUpRecommendedColors(skinTone: String) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvRecommendedColor: RecyclerView = binding.rvRecommended
        rvRecommendedColor.layoutManager = layoutManager

        val recommendedColorList = when (skinTone) {
            "Summer" -> listOf("#00A376", "#B8C9E1", "#FFA3B5", "#9BCBEB", "#F395C7", "#00B0B9", "#9CAF88", "#E277CD", "#7965B2", "#86647A", "#99D6EA", "#3E6991", "#A83D72", "#A05EB5", "#BF0D3E", "#007681", "#F57EB6", "#C964CF", "#26D07C", "#9595D2", "#57728B", "#A39382", "#C6DAE7", "#484A51", "#D592AA", "#CDB5A7", "#A277A6", "#5BC2E7", "#0077C8", "#006F62", "#E31C79", "#ECB3CB", "#C4BFB6", "#CF92B7", "#6787B7", "#963CBD", "#808286", "#F65275", "#A7A2C3", "#AD96DC", "#EF60A3", "#779FB5", "#6DCDB8", "#BCBDBE", "#7C6992", "#93328E", "#AC145A", "#9BB8D3", "#71C5E8", "#C4A4A7", "#003057", "#009CA6", "#DD9CDF", "#A15A95", "#F1BDC8", "#F67599", "#00A9E0", "#5C82A5")
            "Autumn" -> listOf("#007680", "#507F70", "#DAAA00", "#6BCABA", "#C26E60", "#00A3AD", "#94795D", "#52463F", "#A6631B", "#CDA788", "#AE8A79", "#00BFB3", "#5C462B", "#009F4D", "#7A7256", "#CBA052", "#FFCD00", "#B4A91F", "#487A7B", "#FCD299", "#BBC592", "#C4D600", "#007FA3", "#8F993E", "#C4622D", "#00594C", "#BE4D00", "#7C4D3A", "#F5E1A4", "#F68D2E", "#963CBD", "#005A70", "#5D6439", "#B89D18", "#DFD1A7", "#956C58", "#643335", "#9D4815", "#B58150", "#EC5037", "#0087AE", "#205C40", "#5E7E29", "#9A3324", "#63513D", "#67823A", "#FDAA63", "#946037", "#A3AA83", "#D9B48F", "#00778B", "#DB864E", "#A07400", "#EF7200", "#046A38", "#FF8200", "#D69A2D", "#7C2529", "#890C58", "#C05131", "#899064", "#A09958", "#5C068C")
            "Winter" -> listOf("#FEFEFE", "#99D6EA", "#C8D8EB", "#B8C9E1", "#9BB8D3", "#00A3E1", "#59CBE8", "#00B0B9", "#0096CA", "#0087AE", "#0086D6", "#0082BA", "#007FA3", "#0077C8", "#0072CE", "#00617F", "#0057B8", "#004B87", "#003DA5", "#002D72", "#001E62", "#2D389B", "#87189D", "#84329B", "#A51890", "#963CBD", "#C724B1", "#C6007E", "#DA1884", "#E00885", "#E3006D", "#E40046", "#CE0037", "#AA0061", "#890C58", "#991E68", "#5C068C", "#009775", "#009F4D", "#00594C", "#131413", "#341902", "#F395C7", "#F8E59A", "#808286", "#484A51")
            else -> listOf("#FAAA8D", "#FDD26E", "#D9EA9A", "#FF8D6D", "#FF8F1C", "#DA291C", "#2DCCD3", "#FFF9D7", "#A5DFD3", "#A8AD00", "#6CC24A", "#E40046", "#FCE300", "#A6631B", "#00558C", "#C0A392", "#9595D2", "#5C462B", "#009F4D", "#FFB81C", "#FFCD00", "#D539B5", "#84329B", "#74AA50", "#FCC89B", "#D7A9E3", "#C4D600", "#00A499", "#007FA3", "#F0B323", "#BE5400", "#F5E1A4", "#963CBD", "#D4C304", "#A4DBE8", "#D0DEBB", "#D0DF00", "#008EAA", "#77C5D5", "#D3BC8D", "#6DCDB8", "#FBDB65", "#A9C23F", "#EC5037", "#0087AE", "#C5B4E3", "#CAC7A7", "#FDAA63", "#B46A55", "#F8CFA9", "#D9B48F", "#CDA077", "#FFA38B", "#F1EB9C", "#FF8200", "#453536", "#A77BCA", "#9BCBEB")
        }

        rvRecommendedColor.adapter = ColorPaletteAdapter(recommendedColorList)
    }

    private fun setUpAvoidedColors(skinTone: String) {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvAvoidedColor: RecyclerView = binding.rvAvoided
        rvAvoidedColor.layoutManager = layoutManager

        val avoidedColorList = when (skinTone) {
            "Summer" -> listOf("#311C17", "#044022", "#380835", "#52001F", "#8A3324", "#966F33", "#CF1020", "#F18200", "#FFA089", "#FF8040", "#A9561E", "#9F381D", "#D5AB09", "#FFC901", "#F8481C", "#0C0D0F", "#FF0000", "#F400A1", "#FFFF00", "#69D84F", "#FF5A36", "#C7A317", "#40291D")
            "Autumn" -> listOf("#000000", "#FF007F", "#FF3F00", "#FFF600", "#69D84F", "#C1F80A", "#4B0082", "#625D5D", "#FFFFFF", "#BEA6C3", "#9DACB7", "#5F6672", "#9D5783", "#F77FBE", "#434C59", "#7D7F7C", "#FFD1DF", "#AFDCEC", "#ABA0D9", "#D2B9D3", "#D0F0C0", "#A2AAB3", "#0C0D0F")
            "Winter" -> listOf("#492615", "#654321", "#A9561E", "#A62F20", "#F9BF58", "#F9E4BC", "#80B3C4", "#C6AEC7", "#A6814C", "#E7BF05", "#9AB973", "#90B134", "#FEA993", "#FF9A8A", "#B5485D", "#924321", "#C14A09", "#BA6F1E", "#FF9F00", "#D5D195", "#E2CA76", "#FBEA8C", "#DFBE6F")
            else -> listOf("#7F4E1E", "#6A5D1B", "#F9BF58", "#FF7F00", "#C04737", "#5E7D7E", "#A1ADB5", "#B39EB5", "#673147", "#004040", "#560319", "#394851", "#0F0F0F", "#555555", "#4B3621", "#924321", "#F6A4C9", "#CF71AF", "#616D7E", "#959396", "#FFFEFD", "#414257", "#202E54", "#9D0759")
        }

        rvAvoidedColor.adapter = ColorPaletteAdapter(avoidedColorList)
    }

    companion object {
        const val EXTRA_PREDICTION_MODEL = "EXTRA_PREDICTION_MODEL"
    }

}