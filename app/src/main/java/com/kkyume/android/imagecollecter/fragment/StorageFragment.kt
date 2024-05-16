package com.kkyume.android.imagecollecter.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.kkyume.android.imagecollecter.PreviewActivity
import com.kkyume.android.imagecollecter.R
import com.kkyume.android.imagecollecter.adapter.StorageImageAdapter
import com.kkyume.android.imagecollecter.databinding.FragmentStorageImageBinding
import com.kkyume.android.imagecollecter.model.CombinedStoredListData

class StorageFragment : Fragment(), StorageImageAdapter.OnClickListener {

    private lateinit var binding: FragmentStorageImageBinding
    private lateinit var storageAdapter: StorageImageAdapter
    private var sharedPreferences: SharedPreferences? = null

    private val sharedPreferencesListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            updateRecyclerView()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_storage_image,
            container,
            false
        )

        storageAdapter = StorageImageAdapter(requireActivity(), this)
        binding.rvSearchImage.adapter = storageAdapter
        binding.rvSearchImage.layoutManager = GridLayoutManager(requireContext(), 2)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences =
            context.getSharedPreferences("STORAGE_ITEMS", Context.MODE_PRIVATE)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    private fun updateRecyclerView() {
        val itemList = getStoredItems()
        if (itemList.isEmpty()) {
            binding.rvSearchImage.visibility = View.GONE
        } else {
            storageAdapter.submitList(itemList)
            binding.rvSearchImage.visibility = View.VISIBLE
        }
    }

    private fun getStoredItems(): List<CombinedStoredListData> {
        val jsonString = sharedPreferences?.getString("STORAGE_ITEMS", "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<CombinedStoredListData>>() {}.type)
        }
    }

    private fun saveItemToSharedPreferences(item: CombinedStoredListData) {
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val json = gson.toJson(item)
        editor?.putString(item.contents!!, json) // 아이템의 URL을 키로 사용
        editor?.apply()

        updateRecyclerView()

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

    private fun removeItemFromSharedPreferences(position: Int) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        if (position >= 0 && position < favoriteItems.size) {
            favoriteItems.removeAt(position)
            savePrefsStorageItems(favoriteItems)
            updateRecyclerView()
        } else {
            println("Invalid index: $position")
        }
    }

    override fun onClick(position: Int, select: Boolean) {
        val item = storageAdapter.currentList.getOrNull(position)
        if (storageAdapter.currentList.size == position) {
            item?.let {
                if (select) {
                    saveItemToSharedPreferences(it)
                } else {
                    removeItemFromSharedPreferences(storageAdapter.currentList.size - 1)
                }
            }
        } else {
            item?.let {
                if (select) {
                    saveItemToSharedPreferences(it)
                } else {
                    removeItemFromSharedPreferences(position)
                }
            }
        }
    }

    override fun onClickPreview(position: Int) {
        val favoriteItems = getPrefsStorageItems().toMutableList()
        val intent = Intent(requireActivity(), PreviewActivity::class.java)
        intent.putExtra("title", favoriteItems[position].title)
        intent.putExtra("thumbnailUrl", favoriteItems[position].thumbnailUrl)
        intent.putExtra("position", position)
        intent.putExtra("isSelect", true)


        startActivity(intent)

    }
}
