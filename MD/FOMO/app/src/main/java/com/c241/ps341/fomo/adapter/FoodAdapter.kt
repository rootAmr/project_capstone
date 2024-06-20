package com.c241.ps341.fomo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.databinding.ItemFoodBinding
import com.c241.ps341.fomo.ui.model.MainViewModel

class FoodAdapter(private val context: Context?, private var isBookmark: Boolean, private val viewModel: MainViewModel) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private val list = ArrayList<FoodDataItem?>()
    private var onItemClickCallBack: OnItemClickCallBack? = null
    private var bookmarkId: Int? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun getList(): List<FoodDataItem?> {
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: List<FoodDataItem?>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class FoodViewHolder(val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: FoodDataItem?, position: Int) {
            binding.apply {
                cvItem.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(data!!)
                }

//                viewModel.getBookmarks().observe(context as LifecycleOwner) {
//                    val myBookmarks = it.filter { value -> value?.userId == viewModel.getId() }
//                    myBookmarks.map { value -> bookmarkId = value?.id!! }
//                    val checkBookmark = myBookmarks.filter { value -> value?.foodId == data?.id }
//
//                    if (checkBookmark.isNotEmpty()) {
//                        isBookmark = true
//                        btnBookmark.setImageResource(R.drawable.ic_bookmark_on)
//                    }
//                }

                btnBookmark.setImageResource(R.drawable.ic_bookmark_on)
                btnBookmark.setColorFilter(Color.GRAY)

//                btnBookmark.setOnClickListener {
//                    isBookmark = !isBookmark
//
//                    if (isBookmark) {
//                        viewModel.postBookmark(data?.id!!).observe(context as LifecycleOwner) {
//                            if (it == "Create new bookmark success") {
//                                btnBookmark.setImageResource(R.drawable.ic_bookmark_on)
//                                tvBookmark.text = formatBookmarkCount(data.bookmarkCounts!! + 1)
//                                Toast.makeText(
//                                    context,
//                                    "Food recipe has been saved on bookmark",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                isBookmark = false
//                                Toast.makeText(
//                                    context,
//                                    "Error",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    } else {
//                        viewModel.deleteBookmark(bookmarkId!!).observe(context as LifecycleOwner) {
//                            if (it == "Delete bookmark success") {
//                                btnBookmark.setImageResource(R.drawable.ic_bookmark_off)
//                                tvBookmark.text = formatBookmarkCount(data?.bookmarkCounts!! - 1)
//                                Toast.makeText(
//                                    context,
//                                    "Food recipe has been unsaved on bookmark",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                isBookmark = true
//                                Toast.makeText(
//                                    context,
//                                    "Error",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                }

                if (data?.image != null) {
                    Glide.with(itemView)
                        .load(data?.image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivPhoto)
                } else {
                    val category = data?.category

                    when (category) {
                        "ayam", "Ayam" -> {
                            ivPhoto.setImageResource(R.drawable.img_chicken)
                        }
                        "tempe", "Tempe" -> {
                            ivPhoto.setImageResource(R.drawable.img_soybean)
                        }
                        "telur", "Telur" -> {
                            ivPhoto.setImageResource(R.drawable.img_tofu)
                        }
                        "ikan", "Ikan" -> {
                            ivPhoto.setImageResource(R.drawable.img_fish)
                        }
                        "udang", "Udang" -> {
                            ivPhoto.setImageResource(R.drawable.img_shrimp)
                        }
                        "sapi", "Sapi" -> {
                            ivPhoto.setImageResource(R.drawable.img_cow)
                        }
                        "kambing", "Kambing" -> {
                            ivPhoto.setImageResource(R.drawable.img_goat)
                        }
                    }
                }
                tvTitle.text = data?.foodName
                tvBookmark.text = formatBookmarkCount(data?.bookmarkCounts!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.FoodViewHolder {
        val view = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder((view))
    }

    override fun onBindViewHolder(holder: FoodAdapter.FoodViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    private fun formatBookmarkCount(count: Int): String {
        return when {
            count >= 1_000_000_000 -> String.format("%.1fB", count / 1_000_000_000.0)
            count >= 100_000_000 -> String.format("%.0fM", count / 1_000_000.0)
            count >= 10_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 100_000 -> String.format("%.0fK", count / 1_000.0)
            count >= 10_000 -> String.format("%.1fK", count / 1_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: FoodDataItem)
    }
}