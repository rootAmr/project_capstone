package com.capstone.foodmood.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodmood.adapter.FoodAdapter
import com.capstone.foodmood.data.FoodData
import com.capstone.foodmood.databinding.FragmentFavoriteBinding
import com.capstone.foodmood.databinding.FragmentHomeBinding
import com.capstone.foodmood.ui.home.HomeViewModel

class FavoriteFragment : Fragment() {

private var _binding: FragmentFavoriteBinding? = null
    private lateinit var adapter: FoodAdapter
  private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = FoodAdapter()
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
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}