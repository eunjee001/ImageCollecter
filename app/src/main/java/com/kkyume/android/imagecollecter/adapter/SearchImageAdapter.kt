package com.kkyume.android.imagecollecter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kkyume.android.imagecollecter.databinding.ItemSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.image.ImageResponse


class SearchImageAdapter : ListAdapter<CombinedListData,SearchImageAdapter.SearchImageViewHolder>(diffUtil) {
    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<CombinedListData>() {
            override fun areContentsTheSame(oldItem: CombinedListData, newItem: CombinedListData) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CombinedListData, newItem: CombinedListData) =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchImageViewHolder {
        return SearchImageViewHolder(ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    inner class SearchImageViewHolder(private val binding:ItemSearchImageBinding) : ViewHolder(binding.root){
        fun bind(item : CombinedListData){
            binding.tvDate.text = item.dateTime.toString()
        }
    }

}

