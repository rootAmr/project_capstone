package com.c241.ps341.fomo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.c241.ps341.fomo.data.CommentData
import com.c241.ps341.fomo.databinding.ItemCommentBinding

class CommentAdapter() :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private val list = ArrayList<CommentData>()

    fun getList(): List<CommentData> {
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: List<CommentData>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommentData) {
            binding.apply {
                tvName.text = data.name
                rbStar.numStars = data.star
                tvDescription.text = data.description
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val view = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}