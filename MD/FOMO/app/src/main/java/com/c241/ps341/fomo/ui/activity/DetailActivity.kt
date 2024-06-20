package com.c241.ps341.fomo.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MainViewModel
    private var isOwn: Boolean = false
    private lateinit var adapter: CommentAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        with(binding) {
            setContentView(root)

            ivBack.setOnClickListener {
                finish()
            }

            Glide.with(this@DetailActivity)
                .load(image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivPhoto)

            isOwn = userId == viewModel.getId()
            tvName.text = foodName

            getUserName(userId!!) {
                tvPublisher.text = "Publisher : $it"
            }

            tvRating.text = "Rating : $rating"
            tvIngredient.text = ingredients
            tvStep.text = steps

            if (isOwn) {
                progressBar.visibility = View.GONE
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
                    val myBookmark = it.filter { data -> data?.userId == viewModel.getId() }
                    val checkBookmark = myBookmark.filter { data -> data?.foodId == id }
                    progressBar.visibility = View.GONE
                    ivBookmark.visibility = View.VISIBLE

                    if (checkBookmark.isNotEmpty()) {
                        isBookmark = true
                        ivBookmark.setImageResource(R.drawable.ic_bookmark2_on)
                    }
                }

                btnBookmark.setOnClickListener {
                    isBookmark = !isBookmark

                    if (isBookmark) {
                        viewModel.postBookmark(id).observe(this@DetailActivity) {
                            ivBookmark.setImageResource(R.drawable.ic_bookmark2_on)
                            Toast.makeText(
                                this@DetailActivity,
                                "Food recipe has been saved on bookmark",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        viewModel.getBookmarks().observe(this@DetailActivity) {
                            val myBookmark = it.filter { data -> data?.userId == viewModel.getId() }
                            val foodId = myBookmark.filter { data -> data?.foodId == id }
                            viewModel.deleteBookmark(foodId[0]?.id!!)
                            ivBookmark.setImageResource(R.drawable.ic_bookmark2_off)
                            Toast.makeText(
                                this@DetailActivity,
                                "Food recipe has been unsaved on bookmark",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(this@DetailActivity)
            recyclerView.setHasFixedSize(true)
            val data = ArrayList<CommentData>()

            viewModel.getRatings().observe(this@DetailActivity) {
                val list = it.filter { data -> data?.foodId == id }
                val myRating = list.filter { data -> data?.userId == viewModel.getId() }

                if (myRating.isNotEmpty()) {
                    btnSubmit.visibility = View.GONE
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
                                            userName ?: "Unknown User",
                                            value.rate!!,
                                            value1?.commentField!!
                                        )
                                    )

                                    adapter.setList(data)
                                }
                            }
                        } else {
                            getUserName(value?.userId!!) { userName ->
                                data.add(
                                    CommentData(
                                        userName ?: "Unknown User",
                                        value.rate!!,
                                        ""
                                    )
                                )

                                adapter.setList(data)
                            }
                        }
                    }

                    recyclerView.adapter = adapter
                }
            }

            btnSubmit.setOnClickListener {
                Intent(this@DetailActivity, CommentActivity::class.java).also {
                    it.putExtra("extra_id", id)
                    startActivity(it)
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
}