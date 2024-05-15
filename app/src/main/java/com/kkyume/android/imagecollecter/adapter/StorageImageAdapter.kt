package com.kkyume.android.imagecollecter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.kkyume.android.imagecollecter.R
import com.kkyume.android.imagecollecter.databinding.ItemSearchImageBinding
import com.kkyume.android.imagecollecter.databinding.ItemStorageImageBinding
import com.kkyume.android.imagecollecter.model.CombinedStoredListData
import com.kkyume.android.imagecollecter.model.ItemType
import com.kkyume.android.imagecollecter.model.image.ImageResponse
import java.text.SimpleDateFormat


class StorageImageAdapter(
    private val context: Context,
    private val clickListener: StorageImageAdapter.OnClickListener
) : ListAdapter<CombinedStoredListData, StorageImageAdapter.StorageImageViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CombinedStoredListData>() {
            override fun areItemsTheSame(oldItem: CombinedStoredListData, newItem: CombinedStoredListData) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CombinedStoredListData, newItem: CombinedStoredListData) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageImageViewHolder {
        val binding =
            ItemStorageImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StorageImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StorageImageViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.onClickPreview(position)
        }
        holder.ivFavorite.isSelected = true
        holder.ivFavorite.setOnClickListener {
            val isBookmarked = !holder.ivFavorite.isSelected
            holder.ivFavorite.isSelected = isBookmarked
            clickListener.onClick(position, holder.ivFavorite.isSelected)
        }

    }

    inner class StorageImageViewHolder(private val binding: ItemStorageImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivFavorite = binding.ivFavorite

        @SuppressLint("SimpleDateFormat")
        fun bind(item: CombinedStoredListData) {
            // 아이템 정보를 뷰에 표시
            binding.tvTitle.text = item.title
            val dateFormat = "yyyy년 MM월 dd일 HH시 mm분 ss초"
            val simpleDateFormat = SimpleDateFormat(dateFormat)

            val simpleDate: String? = item.date?.let { simpleDateFormat.format(it) }
            binding.tvDate.text = simpleDate
            Glide.with(context)
                .load(item.thumbnailUrl)
                .into(binding.ivSearch)
            if (item.type == ItemType.IMAGE) {
                binding.ivTitleIcon.setImageResource(R.drawable.icon_image)
            } else {
                binding.ivTitleIcon.setImageResource(R.drawable.icon_video)

            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, select: Boolean)
        fun onClickPreview(position: Int)

    }
}
