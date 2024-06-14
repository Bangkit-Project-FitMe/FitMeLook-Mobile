package com.example.fitme.home.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.FitMeRepository
import com.example.fitme.R
import com.example.fitme.adapter.ItemDayAdapter
import com.example.fitme.adapter.ItemImageAdapter
import com.example.fitme.api.ApiConfig
import com.example.fitme.databinding.FragmentHistoryBinding
import com.example.fitme.profile.pref.UserPreference
import com.example.fitme.profile.pref.dataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var auth: FirebaseAuth

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
                val dayAdapter = ItemDayAdapter(historyData)
                parentRecyclerView.adapter = dayAdapter
            } else {
                Log.d("HistoryFragment", "No data received")
            }
        }

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            fetchHistoryData(userId)
        } else {
            Log.e("HistoryFragment", "User is not signed in")
        }

        return root
    }

    private fun fetchHistoryData(userId: String) {
        val apiService = ApiConfig.getApiService("")
        historyViewModel.fetchHistoryData(apiService, userId)
        Log.d("HistoryFragment", "User ID: $userId")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
