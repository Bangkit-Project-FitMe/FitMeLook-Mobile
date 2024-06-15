package com.example.fitme.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import com.example.fitme.R
import com.yalantis.ucrop.UCrop
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
            binding.progressBar.visibility = View.VISIBLE
            uCrop(currentImageUri!!)
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
            binding.progressBar.visibility = View.VISIBLE
            uCrop(uri)
        }
    }

    private fun showImage(imageUri: Uri?) {
        binding.progressBar.visibility = View.GONE
        val image = imageUri?.let { uriToFile(it, requireContext()).reduceFileImage() }
        Log.d("HomeFragment", "Current image file: ${image?.absolutePath}")
        val intent = Intent(requireContext(), ConfirmationActivity::class.java).apply {
            putExtra(ConfirmationActivity.EXTRA_IMAGE_FILE, image)
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private val uCropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK ) {
            val resultUri = UCrop.getOutput(result.data!!)
            showImage(resultUri)
            Log.d("HomeFragment", "Berhasil")
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            Toast.makeText(requireContext(), cropError?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun uCrop(uri: Uri) {
        binding.progressBar.visibility = View.GONE
        val destinationUri = Uri.fromFile(createTempFile("image_ucrop", ".jpg"))
        val options = UCrop.Options().apply {
            setToolbarColor(ContextCompat.getColor(requireContext(), R.color.orange))
            setToolbarWidgetColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        val uCropIntent = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .getIntent(requireContext())

        AlertDialog.Builder(requireContext())
            .setMessage("Please crop focus only on the face. Make sure your hair is at least visible!")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                uCropLauncher.launch(uCropIntent)
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}