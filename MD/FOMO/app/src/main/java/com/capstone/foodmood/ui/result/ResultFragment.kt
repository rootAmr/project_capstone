package com.capstone.foodmood.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.foodmood.adapter.FoodAdapter
import com.capstone.foodmood.data.FoodData
import com.capstone.foodmood.databinding.FragmentHomeBinding
import com.capstone.foodmood.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val resultViewModel =
            ViewModelProvider(this)[ResultViewModel::class.java]

        _binding = FragmentResultBinding.inflate(inflater, container, false)
        adapter = FoodAdapter()
        val root: View = binding.root
        val data = ArrayList<FoodData>()
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

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    Toast.makeText(activity, searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }

//    val textView: TextView = binding.textHome
//    homeViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
//    }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}