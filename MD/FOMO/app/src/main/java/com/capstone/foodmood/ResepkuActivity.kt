package com.capstone.foodmood

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.foodmood.adapter.FoodAdapter
import com.capstone.foodmood.adapter.MyFoodAdapter
import com.capstone.foodmood.data.FoodData
import com.capstone.foodmood.databinding.ActivityHomeBinding
import com.capstone.foodmood.databinding.ActivityResepkuBinding
import com.capstone.foodmood.databinding.FragmentFavoriteBinding
import com.capstone.foodmood.ui.home.HomeViewModel

class ResepkuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResepkuBinding
    private lateinit var adapter: MyFoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResepkuBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        adapter = MyFoodAdapter()
        val root: View = binding.root
        val data = ArrayList<FoodData>()
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        data.add(FoodData("data1", "img1", 0))
        data.add(FoodData("data2", "img2", 0))
        adapter.setList(data)
    }
}