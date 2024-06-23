package com.c241.ps341.fomo.ui.activity

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.c241.ps341.fomo.databinding.ActivityCommentBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]
        val id = intent.getIntExtra("extra_id", 0)

        with(binding) {
            setContentView(root)

            ivBack.setOnClickListener {
                finish()
            }

            btnSubmit.setOnClickListener {
                val progressDialog = ProgressDialog.show(this@CommentActivity, null, "Harap tunggu")
                val star = rbStar.rating.toInt()
                val description = etDescription.text.toString()

                if (star > 0) {
                    viewModel.postRating(id, star).observe(this@CommentActivity) {
                        if (it == "create new Rating success") {
                            if (description.isNotEmpty()) {
                                viewModel.postComment(id, description).observe(this@CommentActivity) { it1 ->
                                    if (it1 == "create new Comment success") {
                                        progressDialog.dismiss()
                                        setResult(Activity.RESULT_OK)
                                        Toast.makeText(
                                            this@CommentActivity,
                                            "Comment has been added",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        progressDialog.dismiss()
                                        Toast.makeText(
                                            this@CommentActivity,
                                            "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                progressDialog.dismiss()
                                setResult(Activity.RESULT_OK)
                                Toast.makeText(
                                    this@CommentActivity,
                                    "Comment has been added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(
                                this@CommentActivity,
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@CommentActivity,
                        "Star must be 1 and up",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}