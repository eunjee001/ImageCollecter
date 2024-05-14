package com.kkyume.android.imagecollecter.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.kkyume.android.imagecollecter.databinding.ItemSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData


class SearchImageAdapter(val context :Context, private val clickListener: OnClickListener) : ListAdapter<CombinedListData,SearchImageAdapter.SearchImageViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<CombinedListData>() {
            override fun areContentsTheSame(oldItem: CombinedListData, newItem: CombinedListData) =
                oldItem.contents == newItem.contents

            override fun areItemsTheSame(oldItem: CombinedListData, newItem: CombinedListData) =
                oldItem.contents === newItem.contents
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchImageViewHolder {
        return SearchImageViewHolder(ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchImageViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.ivFavorite.setOnClickListener {
            val isBookmarked = !holder.ivFavorite.isSelected
            holder.ivFavorite.isSelected = isBookmarked
            clickListener.onClick(position)
        }
    }
    inner class SearchImageViewHolder(private val binding:ItemSearchImageBinding) : ViewHolder(binding.root){
        val ivFavorite = binding.ivFavorite

        fun bind(item : CombinedListData){
            binding.tvDate.text = item.dateTime.toString()
            Glide.with(context)
                .load(item.thumbnail)
                .into(binding.ivSearch)

            binding.tvTitle.text = item.title
            binding.tvUrl.text = item.contents
            binding.tvTitleCollection.text = item.category
        }
    }
    interface OnClickListener{
        fun onClick(position: Int)
    }
}

