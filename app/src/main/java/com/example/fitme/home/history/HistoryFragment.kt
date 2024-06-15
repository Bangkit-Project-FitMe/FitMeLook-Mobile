package com.example.fitme.home.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.adapter.ItemDayAdapter
import com.example.fitme.api.ApiConfig
import com.example.fitme.api.ApiService
import com.example.fitme.api.response.ListHistoryData
import com.example.fitme.databinding.FragmentHistoryBinding
import com.example.fitme.prediction.ResultActivity
import com.google.firebase.auth.FirebaseAuth


class HistoryFragment : Fragment(), ItemDayAdapter.OnItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val parentRecyclerView: RecyclerView = binding.parentRecyclerView
        parentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.historyData.observe(viewLifecycleOwner) { historyData ->
            if (historyData != null) {
                Log.d("HistoryFragment", "Data received: $historyData")
                val dayAdapter = ItemDayAdapter(historyData, this)
                parentRecyclerView.adapter = dayAdapter
            } else {
                Log.d("HistoryFragment", "No data received")
            }
        }

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        apiService = ApiConfig.getApiService()

        if (currentUser != null) {
            val userId = currentUser.uid
            fetchHistoryData(userId)
        } else {
            Log.e("HistoryFragment", "User is not signed in")
        }

        return root
    }

    private fun fetchHistoryData(userId: String) {
        historyViewModel.fetchHistoryData(apiService, userId)
        Log.d("HistoryFragment", "User ID: $userId")
    }

    override fun onItemClick(historyData: ListHistoryData) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("PREDICTION_ID", historyData.prediction_id)
                putExtra("FROM_HISTORY", true)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
