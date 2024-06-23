package com.c241.ps341.fomo.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.adapter.FoodAdapter
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.databinding.FragmentFoodBinding
import com.c241.ps341.fomo.ui.activity.DetailActivity
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory

@Suppress("DEPRECATION")
class FoodFragment : Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FoodAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requireActivity().window.statusBarColor = Color.TRANSPARENT
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        adapter = FoodAdapter(context, false, viewModel)
        val root: View = binding.root
        val categoryId = arguments?.getInt("viewId")
        var category = ""
        var query = arguments?.getString("query")!!

        when (categoryId) {
            R.id.btn_category1 -> {
                category = "Ayam"
            }

            R.id.btn_category2 -> {
                category = "Ikan"
            }

            R.id.btn_category3 -> {
                category = "Telur"
            }

            R.id.btn_category4 -> {
                category = "Kambing"
            }

            R.id.btn_category5 -> {
                category = "Tahu"
            }

            R.id.btn_category6 -> {
                category = "Udang"
            }

            R.id.btn_category7 -> {
                category = "Tempe"
            }

            R.id.btn_category8 -> {
                category = "Sapi"
            }
        }

        adapter.setOnItemClickCallBack(object :
            FoodAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: FoodDataItem) {
                Intent(activity, DetailActivity::class.java).also {
                    it.putExtra("extra_foodname", data.foodName)
                    it.putExtra("extra_image", data.image)
                    it.putExtra("extra_ingredients", data.ingredients)
                    it.putExtra("extra_steps", data.steps)
                    it.putExtra("extra_userid", data.userId)
                    it.putExtra("extra_id", data.id)
                    it.putExtra("extra_rating", data.rating)
                    it.putExtra("extra_category", data.category)
                    startActivity(it)
                }
            }
        })

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            if (query.isNotEmpty()) {
                searchBar.setText(query)
                viewModel.postSearch(query).observe(viewLifecycleOwner) {
                    progressBar.visibility = View.GONE
                    adapter.setList(it)
                    recyclerView.adapter = adapter

                    if (adapter.itemCount == 0) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        tvEmpty.visibility = View.GONE
                    }
                }
            } else {
                viewModel.getFoods().observe(viewLifecycleOwner) {
                    val list = it.filter { data -> data?.category == category }
                    progressBar.visibility = View.GONE
                    adapter.setList(list)
                    recyclerView.adapter = adapter

                    if (adapter.itemCount == 0) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        tvEmpty.visibility = View.GONE
                    }
                }
            }

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                query = searchView.text.toString()

                if (query.isNotEmpty()) {
                    if (category.isEmpty()) {
                        viewModel.postSearch(query).observe(viewLifecycleOwner) {
                            progressBar.visibility = View.GONE
                            adapter.setList(it)
                            recyclerView.adapter = adapter

                            if (adapter.itemCount == 0) {
                                tvEmpty.visibility = View.VISIBLE
                            } else {
                                tvEmpty.visibility = View.GONE
                            }
                        }
                    } else {
                        viewModel.postSearch(query).observe(viewLifecycleOwner) {
                            val list = it.filter { data -> data?.category == category }
                            progressBar.visibility = View.GONE
                            adapter.setList(list)
                            recyclerView.adapter = adapter

                            if (adapter.itemCount == 0) {
                                tvEmpty.visibility = View.VISIBLE
                            } else {
                                tvEmpty.visibility = View.GONE
                            }
                        }
                    }
                } else {
                    if (category.isEmpty()) {
                        viewModel.getFoods().observe(viewLifecycleOwner) {
                            progressBar.visibility = View.GONE
                            adapter.setList(it)
                            recyclerView.adapter = adapter

                            if (adapter.itemCount == 0) {
                                tvEmpty.visibility = View.VISIBLE
                            } else {
                                tvEmpty.visibility = View.GONE
                            }
                        }
                    } else {
                        viewModel.getFoods().observe(viewLifecycleOwner) {
                            val list = it.filter { data -> data?.category == category }
                            progressBar.visibility = View.GONE
                            adapter.setList(list)
                            recyclerView.adapter = adapter

                            if (adapter.itemCount == 0) {
                                tvEmpty.visibility = View.VISIBLE
                            } else {
                                tvEmpty.visibility = View.GONE
                            }
                        }
                    }
                }

                true
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}