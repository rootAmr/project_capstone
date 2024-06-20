@file:Suppress("DEPRECATION")

package com.c241.ps341.fomo.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.databinding.FragmentProfileBinding
import com.c241.ps341.fomo.ui.activity.EditActivity
import com.c241.ps341.fomo.ui.activity.LoginActivity
import com.c241.ps341.fomo.ui.activity.MyFoodActivity
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: MainViewModel
    private lateinit var db: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requireActivity().window.statusBarColor = Color.TRANSPARENT
        val root: View = binding.root
        FirebaseApp.initializeApp(requireContext())
        auth = Firebase.auth
        db = Firebase.database
        storage = Firebase.storage

        with(binding) {
            tvName.text = viewModel.getName()
            tvEmail.text = viewModel.getEmail()

            if (viewModel.getPhoto().isNotEmpty()) {
                Glide.with(requireActivity())
                    .load(Uri.parse(viewModel.getPhoto()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivPhoto)
            } else {
                Glide.with(requireActivity())
                    .load(R.drawable.ic_user)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivPhoto)
            }

            ivPhoto.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 1
                    )
                } else {
                    showOptions()
                }
            }

            btnEditProfile.setOnClickListener {
                Intent(activity, EditActivity::class.java).also {
                    it.putExtra("extra_name", tvName.text)
                    it.putExtra("extra_email", tvEmail.text)
                    startActivity(it)
                }
            }

            btnMyRecipe.setOnClickListener {
                startActivity(Intent(activity, MyFoodActivity::class.java))
            }

            btnLogout.setOnClickListener {
                val progressDialog = ProgressDialog.show(requireContext(), null, "Harap tunggu")
                progressDialog.show()
                requireActivity().lifecycleScope.launch {
                    val credentialManager = CredentialManager.create(requireContext())
                    auth.signOut()
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                    progressDialog.dismiss()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showOptions()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                updatePhoto(imageUri)
            } else {
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    val path = MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        imageBitmap,
                        "Title",
                        null
                    )
                    val uri = Uri.parse(path)
                    updatePhoto(uri)
                }
            }
        }
    }

    private fun showOptions() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent, captureIntent))
        startActivityForResult(chooser, 1)
    }

    private fun updatePhoto(uri: Uri) {
        val id = viewModel.getId()
        val progressDialog = ProgressDialog.show(requireContext(), null, "Harap tunggu")
        val storageRef = storage.reference
        val photoRef = storageRef.child("photos/${id}")
        val uploadTask = photoRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                val photoUrl = uri.toString()
                val userRef = db.reference.child("user").child(id)
                val user = mapOf("photoUrl" to photoUrl)

                userRef.updateChildren(user).addOnCompleteListener { task ->
                    progressDialog.dismiss()

                    if (task.isSuccessful) {
                        viewModel.setPhoto(photoUrl)
                        Glide.with(requireActivity())
                            .load(Uri.parse(photoUrl))
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop()
                            .into(binding.ivPhoto)
                        Toast.makeText(
                            requireContext(),
                            "Photo updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update photo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}