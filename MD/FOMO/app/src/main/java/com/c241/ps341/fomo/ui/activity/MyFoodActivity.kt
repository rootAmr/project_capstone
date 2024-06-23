package com.c241.ps341.fomo.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241.ps341.fomo.adapter.MyFoodAdapter
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.databinding.ActivityMyFoodBinding
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory

@Suppress("DEPRECATION")
class MyFoodActivity : AppCompatActivity(), MyFoodAdapter.OnDeleteClickCallback {
    private lateinit var binding: ActivityMyFoodBinding
    private lateinit var adapter: MyFoodAdapter
    private lateinit var viewModel: MainViewModel
    private var foodId: List<Int?> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onStart() {
        super.onStart()
        binding = ActivityMyFoodBinding.inflate(layoutInflater)
        adapter = MyFoodAdapter(true, this)
        viewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        adapter.setOnItemClickCallBack(object : MyFoodAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: FoodDataItem?) {
                Intent(this@MyFoodActivity, DetailActivity::class.java).also {
                    it.putExtra("extra_foodname", data?.foodName)
                    it.putExtra("extra_image", data?.image)
                    it.putExtra("extra_ingredients", data?.ingredients)
                    it.putExtra("extra_steps", data?.steps)
                    it.putExtra("extra_userid", data?.userId)
                    it.putExtra("extra_id", data?.id)
                    it.putExtra("extra_rating", data?.rating)
                    it.putExtra("extra_category", data?.category)
                    startActivity(it)
                }
            }
        })

        with(binding) {
            setContentView(root)
            recyclerView.layoutManager = LinearLayoutManager(this@MyFoodActivity)
            recyclerView.setHasFixedSize(true)

            viewModel.getFoods().observe(this@MyFoodActivity) {
                val list = it.filter { data -> data?.userId == viewModel.getId() }
                foodId = list.map { data -> data?.id }
                Log.i("mgrlog", foodId.toString())
                progressBar.visibility = View.GONE
                adapter.setList(list)
                recyclerView.adapter = adapter

                if (adapter.itemCount == 0) {
                    tvEmpty.visibility = View.VISIBLE
                } else {
                    tvEmpty.visibility = View.GONE
                }
            }

            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDeleteClicked(data: FoodDataItem?, position: Int) {
        AlertDialog.Builder(this).setTitle("Hapus item")
            .setMessage("Apakah anda ingin menghapus item ini dari bookmark?")
            .setCancelable(true)
            .setPositiveButton("IYA") { _, _ ->
                val progressDialog = ProgressDialog.show(this, null, "Harap tunggu")

                viewModel.deleteFood(foodId[position]!!).observe(this@MyFoodActivity) {
                    progressDialog.dismiss()

                    if (it == "delete Food success") {
                        Toast.makeText(
                            this@MyFoodActivity,
                            "Your recipe has been deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } else {
                        Toast.makeText(
                            this@MyFoodActivity,
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("TIDAK") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }
}