package com.kkyume.android.imagecollecter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kkyume.android.imagecollecter.databinding.ItemSearchImageBinding
import com.kkyume.android.imagecollecter.model.image.ImageResponse


class SearchImageAdapter : ListAdapter<ImageResponse,SearchImageAdapter.SearchImageViewHolder>(diffUtil) {
    companion object{
        val diffUtil = object: DiffUtil.ItemCallback<ImageResponse>() {
            override fun areContentsTheSame(oldItem: ImageResponse, newItem: ImageResponse) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: ImageResponse, newItem: ImageResponse) =
                oldItem.documents == newItem.documents
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchImageViewHolder {
        return SearchImageViewHolder(ItemSearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    inner class SearchImageViewHolder(private val binding:ItemSearchImageBinding) : ViewHolder(binding.root){
        fun bind(item : ImageResponse){


        }
    }

}

