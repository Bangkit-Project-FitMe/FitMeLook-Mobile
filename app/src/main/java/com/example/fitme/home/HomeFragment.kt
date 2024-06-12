package com.example.fitme.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitme.adapter.HistoryImageAdapter
import com.example.fitme.databinding.FragmentHomeBinding
import com.example.fitme.prediction.ConfirmationActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var currentImageUri: Uri? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvHome
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val imageList = listOf(0, 0, 0, 0, 0)
        recyclerView.adapter = HistoryImageAdapter(imageList)

        binding.btnTakePhoto.setOnClickListener {
            if (!allPermissionsGranted()) {
                requestPermissionLauncher.launch(HomeFragment.REQUIRED_PERMISSION)
            } else {
                startCamera()
            }
        }

        binding.btnGallery.setOnClickListener {
            if (!allPermissionsGranted()) {
                requestPermissionLauncher.launch(HomeFragment.REQUIRED_PERMISSION)
            } else {
                startGallery()
            }
        }

        return root
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            HomeFragment.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage(currentImageUri)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage(currentImageUri)
        }
    }

    private fun showImage(imageUri: Uri?) {
        val intent = Intent(requireContext(), ConfirmationActivity::class.java)
        intent.putExtra(ConfirmationActivity.EXTRA_IMAGE_URI, imageUri.toString())
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}