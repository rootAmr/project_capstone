package com.c241.ps341.fomo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.api.response.FoodDataItem
import com.c241.ps341.fomo.databinding.ItemMyfoodBinding

class MyFoodAdapter(
    private val isOwner: Boolean,
    private val onDeleteClickCallback: OnDeleteClickCallback?
) : RecyclerView.Adapter<MyFoodAdapter.FoodViewHolder>() {
    private val list = ArrayList<FoodDataItem?>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: List<FoodDataItem?>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class FoodViewHolder(val binding: ItemMyfoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FoodDataItem?, position: Int) {
            binding.apply {
                cvItem.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(data!!)
                }

                if (!isOwner) {
                    bookmark.visibility = View.GONE
                }

                btnDelete.setOnClickListener {
                    onDeleteClickCallback?.onDeleteClicked(data!!, position)
                }

                if (data?.image != null) {
                    Glide.with(itemView)
                        .load(data?.image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivPhoto)
                } else {
                    val category = data?.category

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
                        "Tahu" -> {
                            ivPhoto.setImageResource(R.drawable.img_tofu)
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

                tvTitle.text = data?.foodName
                tvBookmark.text = data?.bookmarkCounts.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = ItemMyfoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder((view))
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallBack {
        fun onItemClicked(data: FoodDataItem?)
    }

    interface OnDeleteClickCallback {
        fun onDeleteClicked(data: FoodDataItem?, position: Int)
    }
}