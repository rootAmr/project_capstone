package com.c241.ps341.fomo.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.adapter.MyFoodAdapter
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.databinding.FragmentBookmarkBinding
import com.c241.ps341.fomo.ui.activity.DetailActivity
import com.c241.ps341.fomo.ui.model.MainViewModel
import com.c241.ps341.fomo.ui.model.ViewModelFactory
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class BookmarkFragment : Fragment(), MyFoodAdapter.OnDeleteClickCallback {
    private var _binding: FragmentBookmarkBinding? = null
    private lateinit var adapter: MyFoodAdapter
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var foodId: List<Int?> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requireActivity().window.statusBarColor = Color.TRANSPARENT
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        adapter = MyFoodAdapter(false, this)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        )[MainViewModel::class.java]
        val root: View = binding.root

        adapter.setOnItemClickCallBack(object : MyFoodAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: FoodDataItem?) {
                Intent(activity, DetailActivity::class.java).also {
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
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)

            lifecycleScope.launch {
                val id = viewModel.getId()

                viewModel.getBookmarks().observe(viewLifecycleOwner) {
                    val myBookmarks = it.filter { data -> data?.userId == id }
                    foodId = myBookmarks.map { data -> data?.foodId }

                    viewModel.getFoods().observe(viewLifecycleOwner) { foods ->
                        val bookmarkFoods = foods.filter { data -> data?.id in foodId }
                        progressBar.visibility = View.GONE
                        adapter.setList(bookmarkFoods)

                        if (adapter.itemCount == 0) {
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            tvEmpty.visibility = View.GONE
                        }

                        recyclerView.adapter = adapter
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteClicked(data: FoodDataItem?, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus item")
            .setMessage("Apakah anda ingin menghapus item ini dari bookmark?")
            .setCancelable(true)
            .setPositiveButton("IYA") { _, _ ->
                val progressDialog = ProgressDialog.show(requireContext(), null, "Harap tunggu")
                viewModel.deleteBookmark(foodId[position]!!).observe(viewLifecycleOwner) {
                    progressDialog.dismiss()

                    if (it == "Delete bookmark success") {
                        Toast.makeText(
                            requireContext(),
                            "Recipe has been deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_reload)
                    } else {
                        Toast.makeText(
                            requireContext(),
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