package com.c241.ps341.fomo.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.adapter.CommentAdapter
import com.c241.ps341.fomo.data.CommentData
import com.c241.ps341.fomo.databinding.ActivityDetailBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.database.database

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel
    private var isOwn: Boolean = false
    private lateinit var adapter: CommentAdapter

    private val editRatingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val id = intent.getIntExtra("extra_id", 0)
                setComment(id)
                binding.btnSubmit.visibility = View.GONE
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_OK)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityDetailBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]
        adapter = CommentAdapter()
        var isBookmark = false
        val foodName = intent.getStringExtra("extra_foodname")
        val image = intent.getStringExtra("extra_image")
        val ingredients = intent.getStringExtra("extra_ingredients")
        val steps = intent.getStringExtra("extra_steps")
        val userId = intent.getStringExtra("extra_userid")
        val id = intent.getIntExtra("extra_id", 0)
        val rating = intent.getFloatExtra("extra_rating", 0.0f)
        val category = intent.getStringExtra("extra_category")

        with(binding) {
            setContentView(root)

            ivBack.setOnClickListener {
                finish()
            }

            if (image != null) {
                Glide.with(this@DetailActivity)
                    .load(image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivPhoto)
            } else {
                when (category) {
                    "Ayam" -> {
                        ivPhoto.setImageResource(R.drawable.img_chicken)
                    }

                    "Tempe" -> {
                        ivPhoto.setImageResource(R.drawable.img_soybean)
                    }

                    "Telur" -> {
                        ivPhoto.setImageResource(R.drawable.img_tofu)
                    }

                    "Ikan" -> {
                        ivPhoto.setImageResource(R.drawable.img_fish)
                    }

                    "Udang" -> {
                        ivPhoto.setImageResource(R.drawable.img_shrimp)
                    }

                    "Sapi" -> {
                        ivPhoto.setImageResource(R.drawable.img_cow)
                    }

                    "Kambing" -> {
                        ivPhoto.setImageResource(R.drawable.img_goat)
                    }
                }
            }

            isOwn = userId == viewModel.getId()
            tvName.text = foodName

            getUserName(userId!!) {
                tvPublisher.text = "Publisher : " + (it ?: "Anonymous")
            }

            tvRating.text = "Rating : $rating"
            tvIngredient.text = ingredients
            tvStep.text = steps

            if (isOwn) {
                ivBookmark.visibility = View.VISIBLE
                ivBookmark.setImageResource(R.drawable.ic_delete)
                ivBookmark.setColorFilter(Color.TRANSPARENT)

                btnBookmark.setOnClickListener {
                    AlertDialog.Builder(this@DetailActivity).setTitle("Hapus item")
                        .setMessage("Apakah anda ingin menghapus item ini?")
                        .setCancelable(true).setPositiveButton("IYA") { _, _ ->
                            val progressDialog =
                                ProgressDialog.show(this@DetailActivity, null, "Harap tunggu")

                            viewModel.deleteFood(id).observe(this@DetailActivity) { value ->
                                progressDialog.dismiss()

                                if (value == "delete Food success") {
                                    setResult(Activity.RESULT_OK)
                                    Toast.makeText(
                                        this@DetailActivity,
                                        "Your recipe has been deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@DetailActivity,
                                        "Error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }.setNegativeButton("TIDAK") { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
            } else {
                viewModel.getBookmarks().observe(this@DetailActivity) {
                    pbBookmark.visibility = View.GONE
                    val myBookmark = it.filter { data -> data?.userId == viewModel.getId() }
                    val checkBookmark = myBookmark.filter { data -> data?.foodId == id }
                    ivBookmark.visibility = View.VISIBLE

                    if (checkBookmark.isNotEmpty()) {
                        isBookmark = true
                        ivBookmark.setImageResource(R.drawable.ic_bookmark2_on)
                    }
                }

                btnBookmark.setOnClickListener {
                    isBookmark = !isBookmark
                    val progressDialog =
                        ProgressDialog.show(this@DetailActivity, null, "Harap tunggu")

                    if (isBookmark) {
                        viewModel.postBookmark(id).observe(this@DetailActivity) {
                            progressDialog.dismiss()

                            if (it == "Create new bookmark success") {
                                ivBookmark.setImageResource(R.drawable.ic_bookmark2_on)
                                setResult(Activity.RESULT_OK)
                                Toast.makeText(
                                    this@DetailActivity,
                                    "Food recipe has been saved on bookmark",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                isBookmark = true
                                Toast.makeText(
                                    this@DetailActivity,
                                    "Error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        viewModel.getBookmarks().observe(this@DetailActivity) {
                            val myBookmark = it.filter { data -> data?.userId == viewModel.getId() }
                            val filterByFoodId = myBookmark.filter { data -> data?.foodId == id }
                            val foodId = filterByFoodId.map { data -> data?.foodId }

                            viewModel.deleteBookmark(foodId[0]!!)
                                .observe(this@DetailActivity) { it1 ->
                                    progressDialog.dismiss()

                                    if (it1 == "Delete bookmark success") {
                                        ivBookmark.setImageResource(R.drawable.ic_bookmark2_off)
                                        setResult(Activity.RESULT_OK)
                                        Toast.makeText(
                                            this@DetailActivity,
                                            "Food recipe has been unsaved on bookmark",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        isBookmark = false
                                        Toast.makeText(
                                            this@DetailActivity,
                                            "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(this@DetailActivity)
            recyclerView.setHasFixedSize(true)
            setComment(id)

            btnSubmit.setOnClickListener {
                Intent(this@DetailActivity, CommentActivity::class.java).also {
                    it.putExtra("extra_id", id)
                    editRatingLauncher.launch(it)
                }
            }
        }
    }

    private fun getUserName(userId: String, callback: (String?) -> Unit) {
        val db = Firebase.database.reference
        val userRef = db.child("user").child(userId)
        userRef.get()
            .addOnSuccessListener {
                if (it != null && it.exists()) {
                    val userName = it.child("name").getValue(String::class.java)
                    callback(userName)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    private fun setComment(id: Int) {
        with(binding) {
            val data = ArrayList<CommentData>()

            viewModel.getRatings().observe(this@DetailActivity) {
                val list = it.filter { data -> data?.foodId == id }
                val myRating = list.filter { data -> data?.userId == viewModel.getId() }

                if (myRating.isEmpty()) {
                    btnSubmit.visibility = View.VISIBLE
                }

                if (list.isEmpty()) {
                    progressBar.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                } else {
                    tvEmpty.visibility = View.GONE
                }

                viewModel.getComments().observe(this@DetailActivity) { it1 ->
                    val filterId = it1.filter { data -> data?.foodId == id }

                    list.forEach { value ->
                        val list1 = filterId.filter { data -> data?.userId == value?.userId }

                        if (list1.isNotEmpty()) {
                            list1.forEach { value1 ->
                                getUserName(value?.userId!!) { userName ->
                                    data.add(
                                        CommentData(
                                            userName ?: "Anonymous",
                                            value.rate!!,
                                            value1?.commentField!!
                                        )
                                    )
                                    progressBar.visibility = View.GONE
                                    adapter.setList(data)
                                    recyclerView.adapter = adapter
                                }
                            }
                        } else {
                            getUserName(value?.userId!!) { userName ->
                                data.add(
                                    CommentData(
                                        userName ?: "Anonymous",
                                        value.rate!!,
                                        ""
                                    )
                                )
                                progressBar.visibility = View.GONE
                                adapter.setList(data)
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }
            }
        }
    }
}