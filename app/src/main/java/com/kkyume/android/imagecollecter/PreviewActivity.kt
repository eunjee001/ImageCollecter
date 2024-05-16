package com.kkyume.android.imagecollecter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kkyume.android.imagecollecter.databinding.ActivityPreviewBinding
import com.kkyume.android.imagecollecter.model.CombinedStoredListData
import java.text.SimpleDateFormat
import java.util.Date


class PreviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPreviewBinding
    private var thumbUrl : String ?= null
    private var title : String ?= null
    private var date : String ?= null
    private var type : String ?= null
    private var dateFormat : Date ?= null
    private var sharedPreferences: SharedPreferences? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        init()
        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun init(){
        sharedPreferences = this.getSharedPreferences("STORAGE_ITEMS", Context.MODE_PRIVATE)
        title = intent.getStringExtra("title")
        thumbUrl = intent.getStringExtra("thumbnailUrl")
        position = intent.getIntExtra("position", -1)
        val isSelect = intent.getBooleanExtra("isSelect", false)
        type = intent.getStringExtra("imageType")
        date = intent.getStringExtra("date")
        val format = SimpleDateFormat("yyyy-MM-dd") // 입력된 문자열의 날짜 형식 지정
        dateFormat = date?.let { format.parse(it) } // 문자열을 Date 객체로 변환
        binding.ivFavorite.isSelected = isSelect
        binding.ivFavorite.setOnClickListener {
            if (!isSelect){
                binding.ivFavorite.isSelected = true
                saveItemToSharedPreferences()
            }else{
                removeItemFromSharedPreferences(position)
                finish()
            }
        }
        binding.tvTitle.text = title
        Glide.with(this)
            .load(thumbUrl)
            .into(binding.ivPerview)
    }
    private fun saveItemToSharedPreferences() {
        val item = CombinedStoredListData(thumbUrl, title, null, null, dateFormat, null,null,true) // 새로운 아이템 생성
        val favoriteItems = getPrefsStorageItems().toMutableList()
        favoriteItems.add(item) // 아이템을 추가
        savePrefsStorageItems(favoriteItems)
    }


    private fun removeItemFromSharedPreferences(position: Int) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        if (position >= 0 && position < favoriteItems.size) {
            favoriteItems.removeAt(position)
            savePrefsStorageItems(favoriteItems)
        } else {
            println("Invalid index: $position")
        }
    }

    private fun getPrefsStorageItems(): List<CombinedStoredListData> {
        val jsonString = sharedPreferences?.getString("STORAGE_ITEMS", "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<CombinedStoredListData>>() {}.type)
        }
    }

    private fun savePrefsStorageItems(items: List<CombinedStoredListData>) {
        val jsonString = Gson().toJson(items)
        sharedPreferences?.edit()?.putString("STORAGE_ITEMS", jsonString)?.apply()
    }
}