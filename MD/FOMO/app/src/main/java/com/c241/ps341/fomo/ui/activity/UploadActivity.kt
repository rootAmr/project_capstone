package com.c241.ps341.fomo.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.databinding.ActivityUploadBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var viewModel: MainViewModel
    private var categorySelected: String = "none"
    private var currentImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        with(binding) {
            setContentView(root)

            ivBack.setOnClickListener {
                finish()
            }

            ivImage.setImageURI(Uri.parse(intent.getStringExtra("extra_uri")))

            ivImage.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@UploadActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this@UploadActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@UploadActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        1
                    )
                } else {
                    showOptions()
                }
            }

            val category =
                listOf("Ayam", "Ikan", "Kambing", "Sapi", "Tahu", "Telur", "Tempe", "Udang")
            val arrayAdapter = ArrayAdapter(this@UploadActivity, R.layout.dropdown_item, category)
            tvCategory.setAdapter(arrayAdapter)

            tvCategory.setOnItemClickListener { _, _, i, _ ->
                categorySelected = category[i]
            }

            btnSubmit.setOnClickListener {
                val name = etName.text.toString()
                val ingredient = etIngredient.text.toString()
                val step = etDescription.text.toString()

                viewModel.postFood(name, ingredient, step, categorySelected).observe(this@UploadActivity) {
                    if (viewModel.foodPostMsg() == "create new food success") {
                        val imageUri = intent.getStringExtra("extra_uri")
                        Log.i("mgrlog", imageUri.toString())
                        val inputStream: InputStream? =
                            contentResolver.openInputStream(Uri.parse(imageUri))
                        val file = File(cacheDir, "upload_image.jpg")
                        val outputStream = FileOutputStream(file)

                        inputStream?.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }

                        viewModel.patchFood(file).observe(this@UploadActivity) { it1 ->
                            if (it1 == "Update Food Success") {
                                Toast.makeText(
                                    this@UploadActivity,
                                    "The form has been uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Intent(this@UploadActivity, DetailActivity::class.java).also { intent ->
                                    intent.putExtra("extra_foodname", name)
                                    intent.putExtra("extra_image", imageUri)
                                    intent.putExtra("extra_ingredients", ingredient)
                                    intent.putExtra("extra_steps", step)
                                    intent.putExtra("extra_userid", viewModel.getId())
                                    intent.putExtra("extra_id", viewModel.foodId())
                                    intent.putExtra("extra_rating", it?.rating)
                                    startActivity(intent)
                                }

                                finish()
                            } else {
                                Toast.makeText(this@UploadActivity, "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(this@UploadActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

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
                currentImage = imageUri.toString()
                val intent = Intent(applicationContext, UploadActivity::class.java)
                intent.putExtra("extra_uri", imageUri.toString())
                startActivity(intent)
                finish()
            } else {
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    val path = MediaStore.Images.Media.insertImage(
                        applicationContext.contentResolver,
                        imageBitmap,
                        "Title",
                        null
                    )
                    val uri = Uri.parse(path)
                    currentImage = path
                    val intent = Intent(applicationContext, UploadActivity::class.java)
                    intent.putExtra("extra_uri", uri.toString())
                    startActivity(intent)
                    finish()
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
}