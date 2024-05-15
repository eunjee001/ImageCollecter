package com.kkyume.android.imagecollecter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.kkyume.android.imagecollecter.R
import com.kkyume.android.imagecollecter.databinding.ItemSearchImageBinding
import com.kkyume.android.imagecollecter.model.CombinedListData
import com.kkyume.android.imagecollecter.model.ItemType
import java.text.SimpleDateFormat


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
        holder.itemView.setOnClickListener {
            clickListener.onClickPreview(position)
        }
        holder.ivFavorite.setOnClickListener {
            val isBookmarked = !holder.ivFavorite.isSelected
            holder.ivFavorite.isSelected = isBookmarked
            clickListener.onClickFavorite(position, isBookmarked)
        }

    }
    inner class SearchImageViewHolder(private val binding:ItemSearchImageBinding) : ViewHolder(binding.root){
        val ivFavorite = binding.ivFavorite

        @SuppressLint("SimpleDateFormat")
        fun bind(item : CombinedListData){
            val dateFormat = "yyyy년 MM월 dd일 HH시 mm분 ss초"
            val simpleDateFormat = SimpleDateFormat(dateFormat)

            val simpleDate: String? = item.date?.let { simpleDateFormat.format(it) }
            binding.tvDate.text = simpleDate

            Glide.with(context)
                .load(item.thumbnailUrl)
                .into(binding.ivSearch)

            binding.tvTitle.text = item.title
            binding.tvUrl.text = item.contents
            binding.tvTitleCollection.text = item.category
            if (item.type == ItemType.IMAGE){
                binding.ivTitleIcon.setImageResource(R.drawable.icon_image)
            }else{
                binding.ivTitleIcon.setImageResource(R.drawable.icon_video)

            }
        }
    }
    interface OnClickListener{
        fun onClickFavorite(position: Int, select : Boolean)
        fun onClickPreview(position: Int)

    }
}

